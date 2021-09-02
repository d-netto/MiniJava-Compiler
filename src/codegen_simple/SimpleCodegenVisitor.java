package codegen_simple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import parser.ast.ClassNode;
import parser.ast.GoalNode;
import parser.ast.MethodDeclNode;
import parser.ast.VarDeclNode;
import parser.ast.base_abs_classes.ExprNode;
import parser.ast.base_abs_classes.StatementNode;
import parser.ast.expression.ArrayAccessExpr;
import parser.ast.expression.LengthExpr;
import parser.ast.expression.MethodCallExpr;
import parser.ast.expression.NewArrayDeclExpr;
import parser.ast.expression.NewObjectDeclExpr;
import parser.ast.expression.NotExpr;
import parser.ast.expression.binary_expr.AddExpr;
import parser.ast.expression.binary_expr.AndExpr;
import parser.ast.expression.binary_expr.DotExpr;
import parser.ast.expression.binary_expr.LtExpr;
import parser.ast.expression.binary_expr.MultExpr;
import parser.ast.expression.binary_expr.SubExpr;
import parser.ast.expression.literals.IdentifierExpr;
import parser.ast.expression.literals.IntExpr;
import parser.ast.expression.singletons.FalseExpr;
import parser.ast.expression.singletons.ThisExpr;
import parser.ast.expression.singletons.TrueExpr;
import parser.ast.statement.BlockStatement;
import parser.ast.statement.IfStatement;
import parser.ast.statement.PrintStatement;
import parser.ast.statement.SetArrayIndexStatement;
import parser.ast.statement.SetVariableStatement;
import parser.ast.statement.WhileStatement;
import semantics.TypesVisitor;
import semantics.types.ClassType;
import semantics.types.MethodType;
import semantics.types.Type;
import utils.Pair;

public class SimpleCodegenVisitor {

  static final int REGISTER_SIZE = 8;
  static final List<String> ARGUMENT_REGISTERS =
      List.of("%rdi", "%rsi", "%rdx", "%rcx", "%r8", "%r9");

  private static <T, V> int findFirstIndex(List<Pair<T, V>> searched, String name) {
    for (int i = 0; i < searched.size(); ++i) {
      if (searched.get(i).first().equals(name)) {
        return i;
      }
    }
    return -1;
  }

  private static <T> int findLastIndex(List<T> searched, String name) {
    for (int i = searched.size() - 1; i >= 0; --i) {
      if (searched.get(i).equals(name)) {
        return i;
      }
    }
    return -1;
  }

  private class ObjectLayout {

    private List<String> fields;
    private List<Pair<String, String>> vTable;

    private ObjectLayout(ClassType classType) {
      fields = new ArrayList<>();
      vTable = new ArrayList<>();
      List<ClassType> allParents = classType.getAllParents();
      Collections.reverse(allParents);
      for (ClassType parentClass : allParents) {
        // handles fields in the class hierarchy (NOTE: we can have field in child class
        // with same name as field in parent class,
        // and in this case, both show up in the object layout)
        for (Pair<String, Type> fieldPair : parentClass.getFieldsSorted()) {
          String fieldName = fieldPair.first();
          fields.add(fieldName);
        }
        // handles methods in the class hierarchy (NOTE: if a method in a child class
        // overrides a method in a parent class,
        // it replaces the corresponding entry in the vTable)
        String parentClassName = parentClass.getClassName();
        for (Pair<String, MethodType> methodPair : parentClass.getMethodsSorted()) {
          String methodName = methodPair.first();
          int firstIndex = findFirstIndex(vTable, methodName);
          Pair<String, String> vTableEntry = new Pair<>(methodName, parentClassName);
          if (firstIndex == -1) {
            vTable.add(vTableEntry);
          } else {
            vTable.set(firstIndex, vTableEntry);
          }
        }
      }
    }

    private List<String> getFields() {
      return fields;
    }

    private List<Pair<String, String>> getVTable() {
      return vTable;
    }
  }

  private int currentBlockNumber;
  private int currentStackSize;
  private StringBuilder dataRegion;
  private StringBuilder textRegion;
  private Set<Pair<String, String>> methodsAlreadyWritten;
  private Map<String, ObjectLayout> objsLayout;
  private TypesVisitor typesVis;

  public SimpleCodegenVisitor(TypesVisitor typesVis) {
    currentBlockNumber = 0;
    currentStackSize = 0;
    dataRegion = new StringBuilder();
    textRegion = new StringBuilder();
    methodsAlreadyWritten = new HashSet<>();
    objsLayout = new HashMap<>();
    for (ClassType classType : typesVis.getClassSymbolTable().values()) {
      objsLayout.put(classType.getClassName(), new ObjectLayout(classType));
    }
    this.typesVis = typesVis;
  }

  public String getDataRegion() {
    return dataRegion.toString();
  }

  public String getTextRegion() {
    return textRegion.toString();
  }

  private void setCurrentClass(ClassNode node) {
    typesVis.setCurrentClass(node);
  }

  private Optional<ClassType> getCurrentClass() {
    return typesVis.getCurrentClass();
  }

  private void setCurrentMethod(MethodDeclNode node) {
    typesVis.setCurrentMethod(node);
  }

  private Optional<MethodType> getCurrentMethod() {
    return typesVis.getCurrentMethod();
  }

  private void getVarAddress(String varName) {
    Optional<ClassType> currentClass = getCurrentClass();
    Optional<MethodType> currentMethod = getCurrentMethod();
    if (currentClass.isPresent()) {
      // case 1: function argument
      int argumentIndex = findFirstIndex(currentMethod.get().getArgumentsSorted(), varName);
      if (currentMethod.get().getArguments().containsKey(varName)) {
        int offset = REGISTER_SIZE * (2 + argumentIndex);
        textRegion.append("\n\t" + String.format("leaq -%d(%%rbp), %%rax", offset));
        return;
      }
      // case 2: variable declared in function scope
      int varDeclIndex = findFirstIndex(currentMethod.get().getVarsDeclSorted(), varName);
      if (varDeclIndex != -1) {
        int offset =
            REGISTER_SIZE * (2 + currentMethod.get().getArgumentsSorted().size() + varDeclIndex);
        textRegion.append("\n\t" + String.format("leaq -%d(%%rbp), %%rax", offset));
        return;
      }
    }
    // case 3: object field
    if (currentClass.isPresent()) {
      List<String> objectFields = objsLayout.get(currentClass.get().getClassName()).getFields();
      int fieldIndex = findLastIndex(objectFields, varName);
      if (fieldIndex != -1) {
        int offset = REGISTER_SIZE * (1 + fieldIndex);
        // "this" is stored in -8(%rbp) --> move it to %rax
        textRegion.append("\n\t" + String.format("movq -%d(%%rbp), %%rax", REGISTER_SIZE));
        // get the corresponding field in "this" and move it to %rax
        textRegion.append("\n\t" + String.format("leaq %d(%%rax), %%rax", offset));
        return;
      }
    }
    throw new AssertionError("This should have failed semantic checks");
  }

  public void visit(IdentifierExpr expr) {
    String idName = expr.getIdentifierName();
    Optional<ClassType> currentClass = getCurrentClass();
    Optional<MethodType> currentMethod = getCurrentMethod();
    if (currentClass.isPresent()) {
      // case 1: function argument
      int argumentIndex = findFirstIndex(currentMethod.get().getArgumentsSorted(), idName);
      if (currentMethod.get().getArguments().containsKey(idName)) {
        int offset = REGISTER_SIZE * (2 + argumentIndex);
        textRegion.append("\n\t" + String.format("movq -%d(%%rbp), %%rax", offset));
        return;
      }
      // case 2: variable declared in function scope
      int varDeclIndex = findFirstIndex(currentMethod.get().getVarsDeclSorted(), idName);
      if (varDeclIndex != -1) {
        int offset =
            REGISTER_SIZE * (2 + currentMethod.get().getArgumentsSorted().size() + varDeclIndex);
        textRegion.append("\n\t" + String.format("movq -%d(%%rbp), %%rax", offset));
        return;
      }
    }
    // case 3: object field
    if (currentClass.isPresent()) {
      int fieldIndex =
          findLastIndex(objsLayout.get(currentClass.get().getClassName()).getFields(), idName);
      if (fieldIndex != -1) {
        int offset = REGISTER_SIZE * (1 + fieldIndex);
        // "this" is stored in -8(%rbp) --> move it to %rax
        textRegion.append("\n\t" + String.format("movq -%d(%%rbp), %%rax", REGISTER_SIZE));
        // get the corresponding field in "this" and move it to %rax
        textRegion.append("\n\t" + String.format("movq %d(%%rax), %%rax", offset));
        return;
      }
    }
    throw new AssertionError("This should have failed semantic checks");
  }

  public void visit(IntExpr expr) {
    textRegion.append(
        "\n\t" + String.format("movq $%d, %%rax", Integer.parseInt(expr.getIntegerVal())));
  }

  public void visit(FalseExpr expr) {
    textRegion.append("\n\t" + "movq $0, %rax");
  }

  public void visit(TrueExpr expr) {
    textRegion.append("\n\t" + "movq $0xFFFFFFFFFFFFFFFF, %rax");
  }

  public void visit(ThisExpr expr) {
    textRegion.append("\n\t" + String.format("movq -%d(%%rbp), %%rax", REGISTER_SIZE));
  }

  public void visit(AddExpr expr) {
    expr.getLeftHandSide().accept(this);
    // push into the stack
    textRegion.append("\n\t" + "pushq %rax");
    ++currentStackSize;
    expr.getRightHandSide().accept(this);
    // retrieve LHS from stack
    textRegion.append("\n\t" + "popq %rdx");
    --currentStackSize;
    textRegion.append("\n\t" + "addq %rdx, %rax");
  }

  public void visit(AndExpr expr) {
    expr.getLeftHandSide().accept(this);
    textRegion.append("\n\t" + "test %rax, %rax");
    int continuationBlockCache = ++currentBlockNumber;
    textRegion.append("\n\t" + String.format("jz block$%d", continuationBlockCache));
    expr.getRightHandSide().accept(this);
    textRegion.append("\n" + String.format("block$%d:", continuationBlockCache));
  }

  public void visit(DotExpr expr) {
    expr.getLeftHandSide().accept(this);
    // the following is safe (doesn't change typesVis' currentClass/Method) because
    // DotExpr doesn't visit a ClassNode or MethodDeclNode
    Type objType = expr.getLeftHandSide().accept(typesVis);
    assert objType.isClassType() : "This should have failed semantic checks";
    int fieldIndex =
        findLastIndex(
            objsLayout.get(((ClassType) objType).getClassName()).getFields(),
            expr.getRightHandSide().getIdentifierName());
    assert fieldIndex != -1 : "This should have failed semantic checks";
    textRegion.append("\n\t" + String.format("movq %d(%%rax), %%rax", REGISTER_SIZE * fieldIndex));
  }

  public void visit(LtExpr expr) {
    expr.getLeftHandSide().accept(this);
    // push into the stack
    textRegion.append("\n\t" + "pushq %rax");
    ++currentStackSize;
    expr.getRightHandSide().accept(this);
    // retrieve LHS from stack
    textRegion.append("\n\t" + "popq %rdx");
    --currentStackSize;
    textRegion.append("\n\t" + "cmpq %rax, %rdx");
    textRegion.append("\n\t" + "movq $0, %rax");
    int continuationBlock = ++currentBlockNumber;
    textRegion.append("\n\t" + String.format("jge block$%d", continuationBlock));
    textRegion.append("\n\t" + "movq $0xFFFFFFFFFFFFFFFF, %rax");
    textRegion.append("\n" + String.format("block$%d:", continuationBlock));
  }

  public void visit(MultExpr expr) {
    expr.getLeftHandSide().accept(this);
    // push into the stack
    textRegion.append("\n\t" + "pushq %rax");
    ++currentStackSize;
    expr.getRightHandSide().accept(this);
    // retrieve LHS from stack
    textRegion.append("\n\t" + "popq %rdx");
    --currentStackSize;
    textRegion.append("\n\t" + "mulq %rdx");
  }

  public void visit(SubExpr expr) {
    // more efficient to compute in this order...
    expr.getRightHandSide().accept(this);
    // push into the stack
    textRegion.append("\n\t" + "pushq %rax");
    ++currentStackSize;
    // and then store LHS on %rax
    expr.getLeftHandSide().accept(this);
    // retrieve RHS from stack
    textRegion.append("\n\t" + "popq %rdx");
    --currentStackSize;
    textRegion.append("\n\t" + "subq %rdx, %rax");
  }

  public void visit(ArrayAccessExpr expr) {
    // get the array reference (which will be in %rax)
    expr.getArray().accept(this);
    // push into the stack
    textRegion.append("\n\t" + "pushq %rax");
    ++currentStackSize;
    expr.getIndex().accept(this);
    // zeroth element of the array stores its length, so increment index by one
    textRegion.append("\n\t" + "incq %rax");
    // retrieve array pointer from stack
    textRegion.append("\n\t" + "popq %rdx");
    --currentStackSize;
    // dereference pointer to array element and move it to %rax
    textRegion.append("\n\t" + String.format("movq (%%rdx, %%rax, %d), %%rax", REGISTER_SIZE));
  }

  public void visit(LengthExpr expr) {
    // get the array reference (which will be in %rax)
    expr.getLenExpr().accept(this);
    // zeroth element of the array stores its length
    textRegion.append("\n\t" + "movq 0(%rax), %rax");
  }

  public void visit(MethodCallExpr expr) {
    // push arguments into the stack
    List<ExprNode> args = expr.getArgs();
    assert args.size() + 1 <= ARGUMENT_REGISTERS.size()
        : "Current implementation doesn't support more than 6 arguments";
    // get pointer to object on which the method is being called
    expr.getObjectSeqExpr().accept(this);
    // push it to stack
    textRegion.append("\n\t" + "pushq %rax");
    ++currentStackSize;
    // dereference pointer to base of vTable and move it to %rax
    textRegion.append("\n\t" + "movq 0(%rax), %rax");
    // the following is safe (doesn't change typesVis' currentClass/Method) because
    // MethodCallExpr doesn't visit a ClassNode or MethodDeclNode
    Type objType = expr.getObjectSeqExpr().accept(typesVis);
    assert objType.isClassType() : "This should have failed semantic checks";
    int methodIndex =
        findFirstIndex(
            objsLayout.get(((ClassType) objType).getClassName()).getVTable(),
            expr.getMethodNameExpr().getIdentifierName());
    assert methodIndex != -1 : "This should have failed semantic checks";
    textRegion.append(
        "\n\t" + String.format("movq %d(%%rax), %%rax", REGISTER_SIZE * (methodIndex + 1)));
    // %rax now stores function pointer: push it to stack
    textRegion.append("\n\t" + "pushq %rax");
    ++currentStackSize;
    // push args into the stack
    for (ExprNode arg : args) {
      arg.accept(this);
      textRegion.append("\n\t" + "pushq %rax");
      ++currentStackSize;
    }
    // pop arguments
    for (int i = 0; i < args.size(); ++i) {
      textRegion.append("\n\t" + String.format("popq %s", ARGUMENT_REGISTERS.get(args.size() - i)));
      --currentStackSize;
    }
    // pop pointer to function
    textRegion.append("\n\t" + "popq %rax");
    --currentStackSize;
    // pop pointer to object on which the method is being called
    textRegion.append("\n\t" + "popq %rdi");
    --currentStackSize;
    int stackSizeCache = currentStackSize;
    if (currentStackSize % 2 == 1) {
      textRegion.append("\n\t" + String.format("subq $%s, %%rsp", REGISTER_SIZE));
      ++currentStackSize;
    }
    textRegion.append("\n\t" + "call *%rax");
    if (stackSizeCache != currentStackSize) {
      textRegion.append("\n\t" + String.format("addq $%s, %%rsp", REGISTER_SIZE));
      --currentStackSize;
    }
  }

  public void visit(NewArrayDeclExpr expr) {
    // %rax will contain the array size
    expr.getSize().accept(this);
    // callee saved register to hold the length
    textRegion.append("\n\t" + "movq %rax, %r12");
    // increment by one because zeroth byte holds size
    textRegion.append("\n\t" + "incq %rax");
    // pass number of blocks and block-size to calloc
    textRegion.append("\n\t" + "movq %rax, %rdi");
    textRegion.append("\n\t" + "movq $8, %rsi");
    int stackSizeCache = currentStackSize;
    // check 16 alignment in stack
    if (currentStackSize % 2 == 1) {
      textRegion.append("\n\t" + String.format("subq $%s, %%rsp", REGISTER_SIZE));
      ++currentStackSize;
    }
    textRegion.append("\n\t" + "call calloc");
    if (stackSizeCache != currentStackSize) {
      textRegion.append("\n\t" + String.format("addq $%s, %%rsp", REGISTER_SIZE));
      --currentStackSize;
    }
    // store length in the zeroth element
    textRegion.append("\n\t" + "movq %r12, 0(%rax)");
  }

  public void visit(NewObjectDeclExpr expr) {
    String objectName = expr.getObjectName();
    int numBytes = objsLayout.get(objectName).getFields().size() + 1;
    textRegion.append("\n\t" + String.format("movq $%d, %%rdi", numBytes));
    textRegion.append("\n\t" + "movq $8, %rsi");
    // check 16 alignment in stack
    int stackSizeCache = currentStackSize;
    if (currentStackSize % 2 == 1) {
      textRegion.append("\n\t" + String.format("subq $%s, %%rsp", REGISTER_SIZE));
      ++currentStackSize;
    }
    textRegion.append("\n\t" + "call calloc");
    if (stackSizeCache != currentStackSize) {
      textRegion.append("\n\t" + String.format("addq $%s, %%rsp", REGISTER_SIZE));
      --currentStackSize;
    }
    textRegion.append("\n\t" + String.format("leaq %s, %%rdx", objectName + "$$"));
    textRegion.append("\n\t" + "movq %rdx, 0(%rax)");
  }

  public void visit(NotExpr expr) {
    expr.getArgument().accept(this);
    textRegion.append("\n\t" + "notq %rax");
  }

  public void visit(BlockStatement statement) {
    for (StatementNode statementNode : statement.getStatements()) {
      statementNode.accept(this);
    }
  }

  public void visit(IfStatement statement) {
    statement.getIfCondition().accept(this);
    int elseBlockCache = ++currentBlockNumber;
    textRegion.append("\n\t" + "test %rax, %rax");
    textRegion.append("\n\t" + String.format("jz block$%d", elseBlockCache));
    statement.getIfBlock().accept(this);
    int loopExitCache = ++currentBlockNumber;
    textRegion.append("\n\t" + String.format("jmp block$%d", loopExitCache));
    textRegion.append("\n" + String.format("block$%d:", elseBlockCache));
    statement.getElseBlock().accept(this);
    textRegion.append("\n" + String.format("block$%d:", loopExitCache));
  }

  public void visit(PrintStatement statement) {
    statement.getPrintExpr().accept(this);
    textRegion.append("\n\t" + "leaq stdout_buffer, %rdi");
    textRegion.append("\n\t" + "movq %rax, %rsi");
    int stackSizeCache = currentStackSize;
    if (currentStackSize % 2 == 1) {
      textRegion.append("\n\t" + String.format("subq $%s, %%rsp", REGISTER_SIZE));
      ++currentStackSize;
    }
    textRegion.append("\n\t" + "call printf");
    if (stackSizeCache != currentStackSize) {
      textRegion.append("\n\t" + String.format("addq $%s, %%rsp", REGISTER_SIZE));
      --currentStackSize;
    }
  }

  public void visit(SetArrayIndexStatement statement) {
    // %rax will contain the address of the array base
    statement.getVarAssigned().accept(this);
    // push pointer to stack
    textRegion.append("\n\t" + "pushq %rax");
    ++currentStackSize;
    // compute index and increment by one (zeroth element stores size)
    statement.getIndex().accept(this);
    textRegion.append("\n\t" + "incq %rax");
    // push it to stack
    textRegion.append("\n\t" + "pushq %rax");
    ++currentStackSize;
    // compute RHS which will be stored in %rax
    statement.getRightHandSide().accept(this);
    // pop index into %r12
    textRegion.append("\n\t" + "popq %r12");
    --currentStackSize;
    // pop array pointer into %rdx
    textRegion.append("\n\t" + "popq %rdx");
    --currentStackSize;
    // move RHS into appropriate array slot
    textRegion.append("\n\t" + String.format("movq %%rax, (%%rdx, %%r12, %d)", REGISTER_SIZE));
  }

  public void visit(SetVariableStatement statement) {
    // %rax will contain the address of the variable
    getVarAddress(statement.getVarAssigned().getIdentifierName());
    // push pointer to stack
    textRegion.append("\n\t" + "pushq %rax");
    ++currentStackSize;
    // compute RHS which will be stored in %rax
    statement.getRightHandSide().accept(this);
    // pop pointer to variable into %rdx
    textRegion.append("\n\t" + "popq %rdx");
    --currentStackSize;
    // move RHS into appropriate array slot
    textRegion.append("\n\t" + "movq %rax, 0(%rdx)");
  }

  public void visit(WhileStatement statement) {
    int whileConditionCache = ++currentBlockNumber;
    textRegion.append("\n" + String.format("block$%d:", whileConditionCache));
    statement.getWhileCondition().accept(this);
    textRegion.append("\n\t" + "test %rax, %rax");
    int continuationBlockCache = ++currentBlockNumber;
    textRegion.append("\n\t" + String.format("jz block$%d", continuationBlockCache));
    int whileLoopCache = ++currentBlockNumber;
    textRegion.append("\n" + String.format("block$%d:", whileLoopCache));
    statement.getWhileBlock().accept(this);
    textRegion.append("\n\t" + String.format("jmp block$%d", whileConditionCache));
    textRegion.append("\n" + String.format("block$%d:", continuationBlockCache));
  }

  public void visit(ClassNode node) {
    setCurrentClass(node);
    Optional<ClassType> currentClass = getCurrentClass();
    dataRegion.append("\n" + String.format("%s$$:", node.getClassName()));
    if (currentClass.get().getExtendsFrom().isPresent()) {
      dataRegion.append(
          "\n\t"
              + String.format(
                  ".quad %s", currentClass.get().getExtendsFrom().get().getClassName() + "$$"));
    } else {
      dataRegion.append("\n\t" + ".quad 0");
    }
    List<Pair<String, String>> currentVTable =
        objsLayout.get(currentClass.get().getClassName()).getVTable();
    for (Pair<String, String> methodPair : currentVTable) {
      dataRegion.append("\n\t" + ".quad " + methodPair.toString());
    }
    dataRegion.append("\n\t" + ".align 16");
    for (MethodDeclNode methodDeclNode : node.getMethodDecls()) {
      Pair<String, String> methodPair =
          currentVTable.get(findFirstIndex(currentVTable, methodDeclNode.getMethodName()));
      if (!(methodsAlreadyWritten.contains(methodPair))) {
        textRegion.append("\n\n" + methodPair.toString() + ":");
        methodDeclNode.accept(this);
        methodsAlreadyWritten.add(methodPair);
      }
    }
  }

  public void visit(GoalNode node) {
    dataRegion.append(".data");
    dataRegion.append("\n" + "stdout_buffer:");
    dataRegion.append("\n\t" + ".string \"%d\\n\"");
    textRegion.append("\n" + ".text");
    textRegion.append("\n" + ".global main");
    textRegion.append("\n\n" + "main:");
    textRegion.append("\n\t" + "pushq %rbp");
    textRegion.append("\n\t" + "movq %rsp, %rbp");
    node.getStatement().accept(this);
    textRegion.append("\n\t" + "movq $0, %rax");
    textRegion.append("\n\t" + "movq %rbp, %rsp");
    textRegion.append("\n\t" + "popq %rbp");
    textRegion.append("\n\t" + "ret");
    for (ClassNode classNode : node.getClasses()) {
      classNode.accept(this);
    }
  }

  public void visit(MethodDeclNode node) {
    setCurrentMethod(node);
    Optional<MethodType> currentMethod = getCurrentMethod();
    textRegion.append("\n\t" + "pushq %rbp");
    textRegion.append("\n\t" + "movq %rsp, %rbp");
    int stackAllocBytes =
        2
            + currentMethod.get().getArgumentsSorted().size()
            + currentMethod.get().getVarsDeclSorted().size();
    // stack needs to be 16 aligned before calling printf, calloc, etc.
    if (stackAllocBytes % 2 == 1) {
      stackAllocBytes += 1;
    }
    // size of stack is initially equal to what we allocate in the
    // preamble
    currentStackSize = stackAllocBytes;
    // stack allocation
    textRegion.append("\n\t" + String.format("subq $%d, %%rsp", REGISTER_SIZE * stackAllocBytes));
    // NOTE: current implementation doesn't support more than
    // 6 arguments (maximum we can fit in register arguments)
    int numberOfArgs = node.getMethodArgs().size() + 1;
    assert numberOfArgs <= ARGUMENT_REGISTERS.size()
        : "Current implementation doesn't support more than 6 arguments";
    for (int i = 0; i < numberOfArgs; ++i) {
      textRegion.append(
          "\n\t"
              + String.format(
                  "movq %s, -%d(%%rbp)", ARGUMENT_REGISTERS.get(i), REGISTER_SIZE * (i + 1)));
    }
    for (VarDeclNode varDeclNode : node.getVarDecls()) {
      varDeclNode.accept(this);
    }
    for (StatementNode statementNode : node.getStatements()) {
      statementNode.accept(this);
    }
    node.getReturnExpr().accept(this);
    textRegion.append("\n\t" + "movq %rbp, %rsp");
    textRegion.append("\n\t" + "popq %rbp");
    textRegion.append("\n\t" + "ret");
  }

  public void visit(VarDeclNode node) {
    Optional<MethodType> currentMethod = getCurrentMethod();
    int varDeclIndex = findFirstIndex(currentMethod.get().getVarsDeclSorted(), node.getVarName());
    assert varDeclIndex != -1 : "This should have failed semantic checks";
    int offset =
        REGISTER_SIZE * (2 + currentMethod.get().getArgumentsSorted().size() + varDeclIndex);
    textRegion.append(String.format("\n\t" + "movq $0, -%d(%%rbp)", offset));
  }
}
