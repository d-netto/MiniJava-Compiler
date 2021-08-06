package semantics;

import java.util.List;

import parser.ast.ClassNode;
import parser.ast.GoalNode;
import parser.ast.MethodDeclNode;
import parser.ast.VarDeclNode;
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
import parser.ast.interfaces.ExprNode;
import parser.ast.interfaces.StatementNode;
import parser.ast.statement.BlockStatement;
import parser.ast.statement.IfStatement;
import parser.ast.statement.PrintStatement;
import parser.ast.statement.SetArrayIndexStatement;
import parser.ast.statement.SetVariableStatement;
import parser.ast.statement.WhileStatement;
import semantics.types.ClassType;
import semantics.types.MethodType;
import semantics.types.Type;
import semantics.types.Variable;
import semantics.types.base_types.BooleanType;
import semantics.types.base_types.IntArrayType;
import semantics.types.base_types.IntType;
import utils.Pair;

public class TypesVisitor {

    private ClassType currentClass;
    private MethodType currentMethod;
    private BuilderVisitor builderVis;

    public TypesVisitor(BuilderVisitor builderVis) {
        this.currentClass = null;
        this.currentMethod = null;
        this.builderVis = builderVis;
    }

    private Variable getVar(String name, int line) {
        if (currentMethod != null) {
            if (currentMethod.getVarsDecl().containsKey(name)) {
                return currentMethod.getVarsDecl().get(name);
            } else {
                for (Pair<String, Variable> pair : currentMethod.getArguments()) {
                    if (pair.first().equals(name)) {
                        return pair.second();
                    }
                }
            }
        }
        if (currentClass != null && currentClass.getFields().containsKey(name)) {
            return currentClass.getFields().get(name);
        }
        throw new AssertionError(String.format("Variable \"%s\" was not defined", name));
    }

    public Type visit(IdentifierExpr expr) {
        return getVar(expr.getIdentifierName(), expr.getLine()).getType();
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
        assert expr.getLeftHandSide().accept(this).isIntType()
                && expr.getRightHandSide().accept(this).isIntType() : String.format("Type mismatch in line %d",
                        expr.getLine());
        return new IntType();
    }

    public Type visit(AndExpr expr) {
        assert expr.getLeftHandSide().accept(this).isBooleanType()
                && expr.getRightHandSide().accept(this).isBooleanType() : "Type mismatch";
        return new BooleanType();
    }

    public Type visit(DotExpr expr) {
        ExprNode leftHandSide = expr.getLeftHandSide();
        IdentifierExpr rightHandSide = (IdentifierExpr) expr.getRightHandSide();
        String rightHandSideName = rightHandSide.getIdentifierName();
        if (leftHandSide instanceof ThisExpr) {
            return currentClass.getMethods().get(rightHandSideName);
        } else if (leftHandSide instanceof IdentifierExpr) {
            String varName = ((IdentifierExpr) leftHandSide).getIdentifierName();
            return ((ClassType) getVar(varName, expr.getLine()).getType()).getMethods().get(rightHandSideName);
        } else if (leftHandSide instanceof NewObjectDeclExpr) {
            String className = ((NewObjectDeclExpr) leftHandSide).getObjectName();
            return builderVis.getClassType(className, expr.getLine()).getMethods().get(rightHandSideName);
        }
        Type leftHandSideType = leftHandSide.accept(this);
        assert leftHandSideType instanceof ClassType : "Internal error in DotExpr";
        ClassType classType = (ClassType) leftHandSideType;
        assert classType.getFields().containsKey(rightHandSideName) : String.format(
                "Field \"%s\" mentioned in line %d has not been defined in class", rightHandSideName, expr.getLine());
        return classType.getMethods().get(rightHandSideName);
    }

    public Type visit(LtExpr expr) {
        assert expr.getLeftHandSide().accept(this).isIntType()
                && expr.getRightHandSide().accept(this).isIntType() : String.format("Type mismatch in line %d",
                        expr.getLine());
        ;
        return new BooleanType();
    }

    public Type visit(MultExpr expr) {
        assert expr.getLeftHandSide().accept(this).isIntType()
                && expr.getRightHandSide().accept(this).isIntType() : String.format("Type mismatch in line %d",
                        expr.getLine());
        ;
        return new IntType();
    }

    public Type visit(SubExpr expr) {
        assert expr.getLeftHandSide().accept(this).isIntType()
                && expr.getRightHandSide().accept(this).isIntType() : String.format("Type mismatch in line %d",
                        expr.getLine());
        ;
        return new IntType();
    }

    public Type visit(ArrayAccessExpr expr) {
        assert expr.getArray().accept(this).isIntArrayType() : String
                .format("Expression in line %d does not define an array", expr.getLine());
        return new IntType();
    }

    public Type visit(LengthExpr expr) {
        assert expr.getLenExpr().accept(this).isIntArrayType() : String
                .format("Expression in line %d does not define an array", expr.getLine());
        ;
        return new IntType();
    }

    public Type visit(MethodCallExpr expr) {
        Type method = expr.getMethodNameExpr().accept(this);
        assert method.isMethodType() : "Internal error in DotExpr";
        List<ExprNode> argListForExpr = expr.getArgs();
        List<Pair<String, Variable>> args = ((MethodType) method).getArguments();
        assert argListForExpr.size() == args.size() : String
                .format("Number of arguments mismatch in method call in line", expr.getLine());
        for (int i = 0; i < argListForExpr.size(); i++) {
            Type argType = argListForExpr.get(i).accept(this);
            assert argType.equals(args.get(i).second().getType()) : String.format("Type mismatch in argument call %s",
                    expr.prettyPrint(""));
        }
        return ((MethodType) method).getReturnType();
    }

    public Type visit(NewArrayDeclExpr expr) {
        return new IntArrayType();
    }

    public Type visit(NewObjectDeclExpr expr) {
        return builderVis.getClassType(expr.getObjectName(), expr.getLine());
    }

    public Type visit(NotExpr expr) {
        assert expr.getArgument().accept(this).isBooleanType() : String.format("Type mismatch in line %d",
                expr.getLine());
        return new BooleanType();
    }

    public void visit(BlockStatement statement) {
        for (StatementNode statementNode : statement.getStatements()) {
            statementNode.accept(this);
        }
    }

    public void visit(IfStatement statement) {
        assert statement.getIfCondition().accept(this).isBooleanType() : String.format("Type mismatch in line %d",
                statement.getLine());
        statement.getIfBlock().accept(this);
        statement.getElseBlock().accept(this);
    }

    public void visit(PrintStatement statement) {
        assert statement.getPrintExpr().accept(this).isIntType() : String.format("Type mismatch in line %d",
                statement.getLine());
    }

    public void visit(SetArrayIndexStatement statement) {
        assert getVar(statement.getVarAssignedName(), statement.getLine()).getType().isIntArrayType() : "Type mismatch";
        assert statement.getIndex().accept(this).isIntType() : String.format("Type mismatch in line %d",
                statement.getLine());
        assert statement.getRightHandSide().accept(this).isIntType() : String.format("Type mismatch in line %d",
                statement.getLine());
    }

    public void visit(SetVariableStatement statement) {
        assert getVar(statement.getVarAssignedName(), statement.getLine()).getType()
                .equals(statement.getRightHandSide().accept(this)) : String.format("Type mismatch in line %d",
                        statement.getLine());
        ;
    }

    public void visit(WhileStatement statement) {
        assert statement.getWhileCondition().accept(this).isBooleanType() : String.format("Type mismatch in line %d",
                statement.getLine());
        ;
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
                .equals(node.getReturnExpr().accept(this)) : "Type mismatch";
    }

    public void visit(VarDeclNode node) {
        return;
    }

}
