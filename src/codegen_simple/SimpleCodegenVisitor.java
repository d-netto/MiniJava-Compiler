package codegen_simple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import parser.ast.ClassNode;
import parser.ast.GoalNode;
import parser.ast.MethodDeclNode;
import parser.ast.VarDeclNode;
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
import semantics.BuilderVisitor;
import semantics.types.ClassType;
import semantics.types.MethodType;
import utils.Pair;

public class SimpleCodegenVisitor {

    static final int BYTE_SIZE = 8;
    static final List<String> REGISTERS = List.of("%rdi", "%rsi", "%rdx", "%rcx", "%r8", "%r9");

    static private <T> int findFirst(List<T> searched, Function<T, Boolean> func) {
        for (int i = 0; i < searched.size(); ++i) {
            if (func.apply(searched.get(i))) {
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
                // and in this case both show up in the object layout)
                parentClass.getFields();
                List<String> fieldsInParent = new ArrayList<>(parentClass.getFields().keySet());
                Collections.sort(fieldsInParent);
                fields.addAll(fieldsInParent);
                // handles methods in the class hierarchy (NOTE: if a method in a child class
                // overrides a method in a parent class,
                // it replaces the corresponding entry in the vTable)
                String parentClassName = parentClass.getClassName();
                List<String> methodsInParent = new ArrayList<>(parentClass.getMethods().keySet());
                Collections.sort(methodsInParent);
                for (String methodName : methodsInParent) {
                    int firstIndex = findFirst(vTable, elt -> elt.second().equals(methodName));
                    Pair<String, String> vTableEntry = new Pair<>(parentClassName, methodName);
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

    private int blockNumber;
    private Optional<ClassType> currentClass;
    private Optional<MethodType> currentMethod;
    private StringBuilder preamble;
    private StringBuilder functionsRegion;
    private Set<Pair<String, String>> methodsAlreadyWritten;
    private Map<String, ObjectLayout> objsLayout;
    private BuilderVisitor builderVis;

    public SimpleCodegenVisitor(BuilderVisitor builderVis) {
        blockNumber = 0;
        currentClass = Optional.empty();
        currentMethod = Optional.empty();
        preamble = new StringBuilder();
        functionsRegion = new StringBuilder();
        methodsAlreadyWritten = new HashSet<>();
        objsLayout = new HashMap<>();
        for (ClassType classType : builderVis.getClassSymbolTable().values()) {
            objsLayout.put(classType.getClassName(), new ObjectLayout(classType));
        }
        this.builderVis = builderVis;
    }

    public String getPreamble() {
        return preamble.toString();
    }

    public String getFunctionsRegion() {
        return functionsRegion.toString();
    }

    private void setCurrentClass(ClassNode node) {
        currentClass = Optional.of(builderVis.getClassType(node.getClassName(), node.getLine()));
    }

    private void setCurrentMethod(MethodDeclNode node) {
        for (ClassType classType : currentClass.get().getAllParents()) {
            if (classType.getMethods().containsKey(node.getMethodName())) {
                currentMethod = Optional.of(classType.getMethods().get(node.getMethodName()));
                return;
            }
        }
        throw new AssertionError(
                String.format("Method \"%s\" used in line %d was not defined in its class or parent classes",
                        node.getMethodName(), node.getLine()));
    }

    public void visit(IdentifierExpr expr) {
        String idName = expr.getIdentifierName();
        if (currentMethod.isPresent()) {
            // case 1: function argument
            int argumentIndex = findFirst(currentMethod.get().getArgumentsSorted(), elt -> elt.first().equals(idName));
            if (currentMethod.get().getArguments().containsKey(idName)) {
                int offset = BYTE_SIZE * (1 + argumentIndex);
                functionsRegion.append("\n\t" + String.format("movq -%d(%%rbp), %%rax", offset));
                return;
            }
            // case 2: variable declared in function scope
            int varDeclIndex = findFirst(currentMethod.get().getVarsDeclSorted(), elt -> elt.first().equals(idName));
            if (varDeclIndex != -1) {
                int offset = BYTE_SIZE * (1 + currentMethod.get().getArgumentsSorted().size() + varDeclIndex);
                functionsRegion.append("\n\t" + String.format("movq -%d(%%rbp), %%rax", offset));
                return;
            }
        }
        // case 3: object field
        if (currentClass.isPresent()) {
            int fieldIndex = findFirst(objsLayout.get(currentClass.get().getClassName()).getFields(),
                    elt -> elt.equals(idName));
            if (fieldIndex != -1) {
                int offset = BYTE_SIZE * fieldIndex;
                // "this" is stored in -8(%rbp) --> move it to %rax
                functionsRegion.append("\n\t" + String.format("movq -%d(%%rax), %%rax", BYTE_SIZE));
                // get the corresponding field in "this" and move it to %rax
                functionsRegion.append("\n\t" + String.format("movq -%d(%%rax), %%rax", offset));
            }
            return;

        }
        throw new AssertionError("This should have failed semantic checks");
    }

    public void visit(IntExpr expr) {
        functionsRegion.append("\n\t" + String.format("movq $%d, %%rax", Integer.parseInt(expr.getIntegerVal())));
    }

    public void visit(FalseExpr expr) {
        functionsRegion.append("\n\t" + "movq $0, %rax");
    }

    public void visit(TrueExpr expr) {
        functionsRegion.append("\n\t" + "movq $1, %rax");
    }

    public void visit(ThisExpr expr) {
        functionsRegion.append("\n\t" + String.format("movq -%d(%%rbp), %%rax", BYTE_SIZE));
    }

    public void visit(AddExpr expr) {
        expr.getRightHandSide().accept(this);
        functionsRegion.append("\n\t" + "movq %rax, %rdx");
        expr.getLeftHandSide().accept(this);
        functionsRegion.append("\n\t" + "addq %rdx, %%rax");
    }

    public void visit(AndExpr expr) {
        // NOTE: implement short circuit
    }

    public void visit(DotExpr expr) {
    }

    public void visit(LtExpr expr) {
    }

    public void visit(MultExpr expr) {
        expr.getRightHandSide().accept(this);
        functionsRegion.append("\n\t" + "movq %rax, %rdx");
        expr.getLeftHandSide().accept(this);
        functionsRegion.append("\n\t" + "mulq %rdx, %rax");
    }

    public void visit(SubExpr expr) {
        expr.getRightHandSide().accept(this);
        functionsRegion.append("\n\t" + "movq %rax, %rdx");
        expr.getLeftHandSide().accept(this);
        functionsRegion.append("\n\t" + "subq %rdx, %rax");
    }

    public void visit(ArrayAccessExpr expr) {
        // get the array reference (which will be in %rax)
        expr.getArray().accept(this);
        // move it to %rdx
        functionsRegion.append("\n\t" + "movq %rax, %rdx");
        expr.getIndex().accept(this);
        // zeroth element of the array stores its length, so need to increment index by
        // one
        functionsRegion.append("\n\t" + "incq %rax");
        // dereference pointer to array element and move it to %rax
        functionsRegion.append("\n\t" + "movq (%rdx, %rax, -8), %rax");
    }

    public void visit(LengthExpr expr) {
        // get the array reference (which will be in %rax)
        expr.getLenExpr().accept(this);
        // zeroth element of the array stores its length
        functionsRegion.append("\n\t" + "movq 0(%rax), %rax");
    }

    public void visit(MethodCallExpr expr) {
        // get pointer to object on which the method is being called
        expr.getObjectSeqExpr().accept(this);
        // move pointer into %rdi
        functionsRegion.append("\n\t" + "movq %rax, %rdi");
        // TODO: handle function arguments and vTable
    }

    public void visit(NewArrayDeclExpr expr) {
        expr.getSize().accept(this);
        // %rax contains the array size, which needs to be incremented by one because
        // zeroth element stores the size
        functionsRegion.append("\n\t" + "incq %rax");
        functionsRegion.append("\n\t" + "movq %rax, %rdi");
        functionsRegion.append("\n\t" + "call calloc");
    }

    public void visit(NewObjectDeclExpr expr) {
        int numBytes = objsLayout.get(expr.getObjectName()).getFields().size() + 1;
        functionsRegion.append("\n\t" + String.format("movq $%d, %%rdi", BYTE_SIZE * numBytes));
        functionsRegion.append("\n\t" + "call calloc");
    }

    public void visit(NotExpr expr) {
        functionsRegion.append("\n\t" + "notq %rax");
    }

    public void visit(BlockStatement statement) {
        for (StatementNode statementNode : statement.getStatements()) {
            statementNode.accept(this);
        }
    }

    public void visit(IfStatement statement) {
    }

    public void visit(PrintStatement statement) {
        statement.getPrintExpr().accept(this);
        functionsRegion.append("\n\t" + "leaq stdout_buffer, %rdi");
        functionsRegion.append("\n\t" + "movq %rax, %rsi");
        functionsRegion.append("\n\t" + "call printf");
    }

    public void visit(SetArrayIndexStatement statement) {
    }

    public void visit(SetVariableStatement statement) {
    }

    public void visit(WhileStatement statement) {
    }

    public void visit(ClassNode node) {
        setCurrentClass(node);
        preamble.append("\n" + String.format("%s$:", node.getClassName()));
        for (MethodDeclNode methodDeclNode : node.getMethodDecls()) {
            List<Pair<String, String>> currentVTable = objsLayout.get(currentClass.get().getClassName()).getVTable();
            Pair<String, String> methodPair = currentVTable
                    .get(findFirst(currentVTable, elt -> elt.second().equals(methodDeclNode.getMethodName())));
            preamble.append(String.format("\n\t" + ".quad %s$%s", methodPair.first(), methodPair.second()));
            if (!(methodsAlreadyWritten.contains(methodPair))) {
                functionsRegion.append("\n\n" + methodPair.toString() + ":");
                methodDeclNode.accept(this);
                methodsAlreadyWritten.add(methodPair);
            }
        }
        preamble.append("\n\t" + ".align 16");
    }

    public void visit(GoalNode node) {
        preamble.append(".data");
        preamble.append("\n" + "stdout_buffer:");
        preamble.append("\n\t" + ".string \"%d\"");
        functionsRegion.append("\n" + ".text");
        functionsRegion.append("\n" + ".global main");
        functionsRegion.append("\n\n" + "main:");
        functionsRegion.append("\n\t" + "pushq %rbp");
        functionsRegion.append("\n\t" + "movq %rsp, %rbp");
        node.getStatement().accept(this);
        functionsRegion.append("\n\t" + "movq $0, %rax");
        functionsRegion.append("\n\t" + "movq %rbp, %rsp");
        functionsRegion.append("\n\t" + "popq %rbp");
        functionsRegion.append("\n\t" + "ret");
        for (ClassNode classNode : node.getClasses()) {
            classNode.accept(this);
        }
    }

    public void visit(MethodDeclNode node) {
        setCurrentMethod(node);
        functionsRegion.append("\n\t" + "pushq %rbp");
        functionsRegion.append("\n\t" + "movq %rsp, %rbp");
        int stackAllocBytes = 1 + currentMethod.get().getArgumentsSorted().size()
                + currentMethod.get().getVarsDeclSorted().size();
        // stack needs to be 16 aligned before calling printf, calloc, etc.
        if (stackAllocBytes % 2 == 1) {
            stackAllocBytes += 1;
        }
        functionsRegion.append("\n\t" + String.format("subq $%d, %%rsp", BYTE_SIZE * stackAllocBytes));
        int numberOfArgs = node.getMethodArgs().size() + 1;
        assert numberOfArgs <= REGISTERS.size() : "Current implementation doesn't support more than 6 arguments";
        for (int i = 0; i < numberOfArgs; ++i) {
            functionsRegion
                    .append("\n\t" + String.format("movq %s, -%d(%%rbp)", REGISTERS.get(i), BYTE_SIZE * (i + 1)));
        }
        for (VarDeclNode varDeclNode : node.getVarDecls()) {
            varDeclNode.accept(this);
        }
        for (StatementNode statementNode : node.getStatements()) {
            statementNode.accept(this);
        }
        node.getReturnExpr().accept(this);
        functionsRegion.append("\n\t" + "movq %rbp, %rsp");
        functionsRegion.append("\n\t" + "popq %rbp");
        functionsRegion.append("\n\t" + "ret");
    }

    public void visit(VarDeclNode node) {
        int varDeclIndex = findFirst(currentMethod.get().getVarsDeclSorted(),
                elt -> elt.first().equals(node.getVarName()));
        assert varDeclIndex != -1 : "This should have failed semantic checks";
        int offset = BYTE_SIZE * (1 + currentMethod.get().getArgumentsSorted().size() + varDeclIndex);
        functionsRegion.append(String.format("movq $0, -%d(%%rbp)", offset));
    }

}
