package semantics;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

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
import semantics.types.ClassType;
import semantics.types.MethodType;
import semantics.types.Type;
import semantics.types.base_types.BooleanType;
import semantics.types.base_types.IntArrayType;
import semantics.types.base_types.IntType;
import utils.Pair;

public class TypesVisitor {

  private Optional<ClassType> currentClass;
  private Optional<MethodType> currentMethod;
  private BuilderVisitor builderVis;

  public TypesVisitor(BuilderVisitor builderVis) {
    this.currentClass = Optional.empty();
    this.currentMethod = Optional.empty();
    this.builderVis = builderVis;
  }

  public Map<String, ClassType> getClassSymbolTable() {
    return builderVis.getClassSymbolTable();
  }

  public ClassType getClassType(String className, int line) {
    return builderVis.getClassType(className, line);
  }

  private Type getVarType(String name, int line) {
    if (currentMethod.isPresent()) {
      if (currentMethod.get().getVarsDecl().containsKey(name)) {
        return currentMethod.get().getVarsDecl().get(name);
      } else if (currentMethod.get().getArguments().containsKey(name)) {
        return currentMethod.get().getArguments().get(name);
      }
    }
    if (currentClass.isPresent()) {
      for (ClassType classType : currentClass.get().getAllParents()) {
        if (classType.getFields().containsKey(name)) {
          return classType.getFields().get(name);
        }
      }
    }
    throw new AssertionError(
        String.format("Type \"%s\" used in line %d was not defined", name, line));
  }

  public void setCurrentClass(ClassNode node) {
    currentClass = Optional.of(builderVis.getClassType(node.getClassName(), node.getLine()));
  }

  public Optional<ClassType> getCurrentClass() {
    return currentClass;
  }

  public void setCurrentMethod(MethodDeclNode node) {
    for (ClassType classType : currentClass.get().getAllParents()) {
      if (classType.getMethods().containsKey(node.getMethodName())) {
        currentMethod = Optional.of(classType.getMethods().get(node.getMethodName()));
        return;
      }
    }
    throw new AssertionError(
        String.format(
            "Method \"%s\" used in line %d was not defined in its class or parent classes",
            node.getMethodName(), node.getLine()));
  }

  public Optional<MethodType> getCurrentMethod() {
    return currentMethod;
  }

  public Type visit(IdentifierExpr expr) {
    return getVarType(expr.getIdentifierName(), expr.getLine());
  }

  public Type visit(IntExpr expr) {
    return new IntType();
  }

  public Type visit(FalseExpr expr) {
    return new BooleanType();
  }

  public Type visit(TrueExpr expr) {
    return new BooleanType();
  }

  public Type visit(ThisExpr expr) {
    return currentClass.get();
  }

  public Type visit(AddExpr expr) {
    Type leftHandSide = expr.getLeftHandSide().accept(this);
    Type rightHandSide = expr.getRightHandSide().accept(this);
    assert leftHandSide.isIntType() && rightHandSide.isIntType()
        : String.format("Type mismatch in line %d", expr.getLine());
    return new IntType();
  }

  public Type visit(AndExpr expr) {
    Type leftHandSide = expr.getLeftHandSide().accept(this);
    Type rightHandSide = expr.getRightHandSide().accept(this);
    assert leftHandSide.isBooleanType() && rightHandSide.isBooleanType()
        : String.format("Type mismatch in line %d", expr.getLine());
    return new BooleanType();
  }

  public Type visit(DotExpr expr) {
    ExprNode leftHandSide = expr.getLeftHandSide();
    IdentifierExpr rightHandSide = (IdentifierExpr) expr.getRightHandSide();
    String rightHandSideName = rightHandSide.getIdentifierName();
    Type leftHandSideType = leftHandSide.accept(this);
    assert leftHandSideType.isClassType() : "Internal error in DotExpr";
    ClassType classType = (ClassType) leftHandSideType;
    assert classType.getFields().containsKey(rightHandSideName)
        : String.format(
            "Method \"%s\" from line %d not defined in its class",
            rightHandSideName, expr.getLine());
    return classType.getFields().get(rightHandSideName);
  }

  public Type visit(LtExpr expr) {
    Type leftHandSide = expr.getLeftHandSide().accept(this);
    Type rightHandSide = expr.getRightHandSide().accept(this);
    assert leftHandSide.isIntType() && rightHandSide.isIntType()
        : String.format("Type mismatch in line %d", expr.getLine());
    return new BooleanType();
  }

  public Type visit(MultExpr expr) {
    Type leftHandSide = expr.getLeftHandSide().accept(this);
    Type rightHandSide = expr.getRightHandSide().accept(this);
    assert leftHandSide.isIntType() && rightHandSide.isIntType()
        : String.format("Type mismatch in line %d", expr.getLine());
    return new IntType();
  }

  public Type visit(SubExpr expr) {
    Type leftHandSide = expr.getLeftHandSide().accept(this);
    Type rightHandSide = expr.getRightHandSide().accept(this);
    assert leftHandSide.isIntType() && rightHandSide.isIntType()
        : String.format("Type mismatch in line %d", expr.getLine());
    return new IntType();
  }

  public Type visit(ArrayAccessExpr expr) {
    Type arrayType = expr.getArray().accept(this);
    Type index = expr.getIndex().accept(this);
    assert arrayType.isIntArrayType() && index.isIntType()
        : String.format("Expression in line %d does not define an array", expr.getLine());
    return new IntType();
  }

  public Type visit(LengthExpr expr) {
    Type arrayType = expr.getLenExpr().accept(this);
    assert arrayType.isIntArrayType()
        : String.format("Expression in line %d does not define an array", expr.getLine());
    return new IntType();
  }

  public Type visit(MethodCallExpr expr) {
    Type objectSeqExpr = expr.getObjectSeqExpr().accept(this);
    assert objectSeqExpr.isClassType()
        : String.format("Predicate defined in line %d doesn't define a class", expr.getLine());
    String methodName = expr.getMethodNameExpr().getIdentifierName();
    Optional<MethodType> method = Optional.empty();
    for (ClassType classType : ((ClassType) objectSeqExpr).getAllParents()) {
      if (classType.getMethods().containsKey(methodName)) {
        method = Optional.of(classType.getMethods().get(methodName));
      }
    }
    assert method.isPresent()
        : String.format(
            "Method \"%s\" used in line %d was not defined in its class or parent classes",
            methodName, expr.getLine());
    Iterator<ExprNode> argListForExprIter = expr.getArgs().iterator();
    Iterator<Pair<String, Type>> argListIter = method.get().getArgumentsSorted().iterator();
    while (argListForExprIter.hasNext()) {
      Type argCallType = argListForExprIter.next().accept(this);
      Type argSigType = argListIter.next().second();
      if (argSigType.isClassType()) {
        assert argCallType.isClassType()
            : String.format(
                "Argument type in function call in line %d should be a non-primitive object",
                expr.getLine());
        assert ((ClassType) argCallType).containsClassAsParent(((ClassType) argSigType))
            : String.format("Type mismatch in argument call in line %d", expr.getLine());
      } else {
        assert argSigType.equals(argCallType)
            : String.format("Type mismatch in argument call in line %d", expr.getLine());
      }
    }
    assert !(argListIter.hasNext())
        : String.format("Number of arguments mismatch in method call in line", expr.getLine());
    return method.get().getReturnType();
  }

  public Type visit(NewArrayDeclExpr expr) {
    Type sizeType = expr.getSize().accept(this);
    assert sizeType.isIntType() : String.format("Type mismatch in line %d", expr.getLine());
    return new IntArrayType();
  }

  public Type visit(NewObjectDeclExpr expr) {
    return builderVis.getClassType(expr.getObjectName(), expr.getLine());
  }

  public Type visit(NotExpr expr) {
    Type argument = expr.getArgument().accept(this);
    assert argument.isBooleanType() : String.format("Type mismatch in line %d", expr.getLine());
    return new BooleanType();
  }

  public void visit(BlockStatement statement) {
    for (StatementNode statementNode : statement.getStatements()) {
      statementNode.accept(this);
    }
  }

  public void visit(IfStatement statement) {
    Type ifCondition = statement.getIfCondition().accept(this);
    assert ifCondition.isBooleanType()
        : String.format("Type mismatch in line %d", statement.getLine());
    statement.getIfBlock().accept(this);
    statement.getElseBlock().accept(this);
  }

  public void visit(PrintStatement statement) {
    Type printExpr = statement.getPrintExpr().accept(this);
    assert printExpr.isIntType() : String.format("Type mismatch in line %d", statement.getLine());
  }

  public void visit(SetArrayIndexStatement statement) {
    Type arrayVariableType = statement.getVarAssigned().accept(this);
    assert arrayVariableType.isIntArrayType()
        : String.format("Type mismatch in line %d", statement.getLine());
    assert statement.getIndex().accept(this).isIntType()
        : String.format("Type mismatch in line %d", statement.getLine());
    assert statement.getRightHandSide().accept(this).isIntType()
        : String.format("Type mismatch in line %d", statement.getLine());
  }

  public void visit(SetVariableStatement statement) {
    Type variableType = statement.getVarAssigned().accept(this);
    Type rightHandSideType = statement.getRightHandSide().accept(this);
    if (variableType.isClassType()) {
      assert rightHandSideType.isClassType()
          : String.format(
              "Right hand side in line %d should be a non-primitive object", statement.getLine());
      assert ((ClassType) rightHandSideType).containsClassAsParent(((ClassType) variableType))
          : String.format("Type mismatch in line %d", statement.getLine());
    } else {
      assert variableType.equals(rightHandSideType)
          : String.format("Type mismatch in line %d", statement.getLine());
    }
  }

  public void visit(WhileStatement statement) {
    Type whileCondition = statement.getWhileCondition().accept(this);
    assert whileCondition.isBooleanType()
        : String.format("Type mismatch in line %d", statement.getLine());
    statement.getWhileBlock().accept(this);
  }

  public void visit(ClassNode node) {
    setCurrentClass(node);
    for (VarDeclNode varDecl : node.getVarDecls()) {
      varDecl.accept(this);
    }
    for (MethodDeclNode methodDecl : node.getMethodDecls()) {
      methodDecl.accept(this);
    }
  }

  public void visit(GoalNode node) {
    node.getStatement().accept(this);
    for (ClassNode classNode : node.getClasses()) {
      classNode.accept(this);
    }
  }

  public void visit(MethodDeclNode node) {
    setCurrentMethod(node);
    for (VarDeclNode varDecl : node.getVarDecls()) {
      varDecl.accept(this);
    }
    for (StatementNode statement : node.getStatements()) {
      statement.accept(this);
    }
    assert builderVis
            .getType(node.getMethodType(), node.getLine())
            .equals(node.getReturnExpr().accept(this))
        : String.format("Type mismatch in line %d", node.getLine());
  }

  public void visit(VarDeclNode node) {
    return;
  }
}
