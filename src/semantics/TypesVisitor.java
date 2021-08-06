package semantics;

import java.util.Iterator;

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
import utils.VariableHolder;

public class TypesVisitor {

    private ClassType currentClass;
    private MethodType currentMethod;
    private BuilderVisitor builderVis;

    public TypesVisitor(BuilderVisitor builderVis) {
        this.currentClass = null;
        this.currentMethod = null;
        this.builderVis = builderVis;
    }

    private Type getVar(String name, int line) {
        if (currentMethod != null) {
            if (currentMethod.getVarsDecl().containsKey(name)) {
                return currentMethod.getVarsDecl().get(name);
            } else {
                for (VariableHolder varHolder : currentMethod.getArguments()) {
                    if (varHolder.getVarName().equals(name)) {
                        return varHolder.getType();
                    }
                }
            }
        }
        if (currentClass != null) {
            for (ClassType classType : currentClass.getAllParents()) {
                if (classType.getFields().containsKey(name)) {
                    return classType.getFields().get(name);
                }
            }
        }
        throw new AssertionError(String.format("Type \"%s\" used in line %d was not defined", name, line));
    }

    public Type visit(IdentifierExpr expr) {
        return getVar(expr.getIdentifierName(), expr.getLine());
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
        return currentClass;
    }

    public Type visit(AddExpr expr) {
        Type leftHandSide = expr.getLeftHandSide().accept(this);
        Type rightHandSide = expr.getRightHandSide().accept(this);
        assert leftHandSide.isIntType() && rightHandSide.isIntType() : String.format("Type mismatch in line %d",
                expr.getLine());
        return new IntType();
    }

    public Type visit(AndExpr expr) {
        Type leftHandSide = expr.getLeftHandSide().accept(this);
        Type rightHandSide = expr.getRightHandSide().accept(this);
        assert leftHandSide.isBooleanType() && rightHandSide.isBooleanType() : String.format("Type mismatch in line %d",
                expr.getLine());
        return new BooleanType();
    }

    public Type visit(DotExpr expr) {
        ExprNode leftHandSide = expr.getLeftHandSide();
        IdentifierExpr rightHandSide = (IdentifierExpr) expr.getRightHandSide();
        String rightHandSideName = rightHandSide.getIdentifierName();
        Type leftHandSideType = leftHandSide.accept(this);
        assert leftHandSideType.isClassType() : "Internal error in DotExpr";
        ClassType classType = (ClassType) leftHandSideType;
        assert classType.getMethods().containsKey(rightHandSideName) : String
                .format("Method \"%s\" from line %d not defined in its class", rightHandSideName, expr.getLine());
        return classType.getMethods().get(rightHandSideName);
    }

    public Type visit(LtExpr expr) {
        Type leftHandSide = expr.getLeftHandSide().accept(this);
        Type rightHandSide = expr.getRightHandSide().accept(this);
        assert leftHandSide.isIntType() && rightHandSide.isIntType() : String.format("Type mismatch in line %d",
                expr.getLine());
        return new BooleanType();
    }

    public Type visit(MultExpr expr) {
        Type leftHandSide = expr.getLeftHandSide().accept(this);
        Type rightHandSide = expr.getRightHandSide().accept(this);
        assert leftHandSide.isIntType() && rightHandSide.isIntType() : String.format("Type mismatch in line %d",
                expr.getLine());
        return new IntType();
    }

    public Type visit(SubExpr expr) {
        Type leftHandSide = expr.getLeftHandSide().accept(this);
        Type rightHandSide = expr.getRightHandSide().accept(this);
        assert leftHandSide.isIntType() && rightHandSide.isIntType() : String.format("Type mismatch in line %d",
                expr.getLine());
        return new IntType();
    }

    public Type visit(ArrayAccessExpr expr) {
        Type arrayType = expr.getArray().accept(this);
        Type index = expr.getIndex().accept(this);
        assert arrayType.isIntArrayType() && index.isIntType() : String
                .format("Expression in line %d does not define an array", expr.getLine());
        return new IntType();
    }

    public Type visit(LengthExpr expr) {
        Type arrayType = expr.getLenExpr().accept(this);
        assert arrayType.isIntArrayType() : String.format("Expression in line %d does not define an array",
                expr.getLine());
        return new IntType();
    }

    public Type visit(MethodCallExpr expr) {
        Type method = expr.getMethodNameExpr().accept(this);
        assert method.isMethodType() : "Internal error in DotExpr";
        Iterator<ExprNode> argListForExprIter = expr.getArgs().iterator();
        Iterator<VariableHolder> argListIter = ((MethodType) method).getArguments().iterator();
        while (argListForExprIter.hasNext()) {
            Type argCallType = argListForExprIter.next().accept(this);
            Type argSigType = argListIter.next().getType();
            if (argSigType.isClassType()) {
                assert argCallType.isClassType() : String
                        .format("Argument type in function call in line %d should be a class", expr.getLine());
                assert ((ClassType) argCallType).containsClassAsParent(((ClassType) argSigType)) : String
                        .format("Type mismatch in argument call in line %d", expr.getLine());
            } else {
                assert argSigType.equals(argCallType) : String.format("Type mismatch in argument call in line %d",
                        expr.getLine());
            }
        }
        assert !(argListIter.hasNext()) : String.format("Number of arguments mismatch in method call in line",
                expr.getLine());
        return ((MethodType) method).getReturnType();
    }

    public Type visit(NewArrayDeclExpr expr) {
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
        assert ifCondition.isBooleanType() : String.format("Type mismatch in line %d", statement.getLine());
        statement.getIfBlock().accept(this);
        statement.getElseBlock().accept(this);
    }

    public void visit(PrintStatement statement) {
        Type printExpr = statement.getPrintExpr().accept(this);
        assert printExpr.isIntType() : String.format("Type mismatch in line %d", statement.getLine());
    }

    public void visit(SetArrayIndexStatement statement) {
        Type arrayVariable = getVar(statement.getVarAssignedName(), statement.getLine());
        assert arrayVariable.isIntArrayType() : String.format("Type mismatch in line %d", statement.getLine());
        assert statement.getIndex().accept(this).isIntType() : String.format("Type mismatch in line %d",
                statement.getLine());
        assert statement.getRightHandSide().accept(this).isIntType() : String.format("Type mismatch in line %d",
                statement.getLine());
    }

    public void visit(SetVariableStatement statement) {
        Type variable = getVar(statement.getVarAssignedName(), statement.getLine());
        Type rightHandSideType = statement.getRightHandSide().accept(this);
        if (variable.isClassType()) {
            assert rightHandSideType.isClassType() : String
                    .format("Right hand side in line %d should be a memeber of a class", statement.getLine());
            assert ((ClassType) rightHandSideType).containsClassAsParent(((ClassType) variable)) : String
                    .format("Type mismatch in line %d", statement.getLine());
        } else {
            assert variable.equals(rightHandSideType) : String.format("Type mismatch in line %d", statement.getLine());
        }
    }

    public void visit(WhileStatement statement) {
        Type whileCondition = statement.getWhileCondition().accept(this);
        assert whileCondition.isBooleanType() : String.format("Type mismatch in line %d", statement.getLine());
        statement.getWhileBlock().accept(this);
    }

    public void visit(ClassNode node) {
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
            currentClass = builderVis.getClassType(classNode.getClassName(), node.getLine());
            classNode.accept(this);
        }
    }

    public void visit(MethodDeclNode node) {
        currentMethod = currentClass.getMethods().get(node.getMethodName());
        for (VarDeclNode varDecl : node.getVarDecls()) {
            varDecl.accept(this);
        }
        for (StatementNode statement : node.getStatements()) {
            statement.accept(this);
        }
        assert builderVis.getType(node.getMethodType(), node.getLine())
                .equals(node.getReturnExpr().accept(this)) : String.format("Type mismatch in line %d", node.getLine());
    }

    public void visit(VarDeclNode node) {
        return;
    }

}
