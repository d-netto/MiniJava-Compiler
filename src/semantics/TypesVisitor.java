package semantics;

import java.util.List;

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
import semantics.types.ClassType;
import semantics.types.MethodType;
import semantics.types.Type;
import semantics.types.Variable;
import semantics.types.base_types.BooleanType;
import semantics.types.base_types.IntType;
import utils.Pair;

public class TypesVisitor {

    private ClassType currentClass;
    private MethodType currentMethod;
    private BuilderVisitor builderVis;

    private Type getVar(String name) {
        if (currentMethod != null) {
            if (currentMethod.getVarsDecl().containsKey(name)) {
                return currentMethod.getVarsDecl().get(name).getType();
            } else {
                for (Pair<String, Variable> pair : currentMethod.getArguments()) {
                    if (pair.first().equals(name)) {
                        return pair.second();
                    }
                }
            }
        } else if (currentClass != null && currentClass.getFields().containsKey(name)) {
            return currentClass.getFields().get(name);
        }
        throw new AssertionError(String.format("Variable \"%s\" was not defined", name));
    }

    public Type visit(IdentifierExpr expr) {
        return getVar(expr.getIdentifierName());
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
                && expr.getRightHandSide().accept(this).isIntType() : "Type mismatch";
        return new IntType();
    }

    public Type visit(AndExpr expr) {
        assert expr.getLeftHandSide().accept(this).isBooleanType()
                && expr.getRightHandSide().accept(this).isBooleanType() : "Type mismatch";
        return new BooleanType();
    }

    public Type visit(DotExpr expr) {
        ExprNode leftHandSide = expr.getLeftHandSide();
        IdentifierExpr rightHandSide = (IdentifierExpr) expr.getLeftHandSide();
        String rightHandSideName = rightHandSide.getIdentifierName();
        if (leftHandSide instanceof IdentifierExpr) {
            String className = ((IdentifierExpr) leftHandSide).getIdentifierName();
            return builderVis.getClassType(className).getMethods().get(rightHandSideName);
        }
        Type leftHandSideType = leftHandSide.accept(this);
        assert leftHandSideType instanceof ClassType : "Internal error in DotExpr";
        ClassType classType = (ClassType) leftHandSideType;
        assert classType.getFields().containsKey(rightHandSideName) : String
                .format("Field \"%s\" has not been defined in class", rightHandSideName);
        return classType.getFields().get(rightHandSideName);
    }

    public Type visit(LtExpr expr) {
        assert expr.getLeftHandSide().accept(this).isIntType()
                && expr.getRightHandSide().accept(this).isIntType() : "Type mismatch";
        return new BooleanType();
    }

    public Type visit(MultExpr expr) {
        assert expr.getLeftHandSide().accept(this).isIntType()
                && expr.getRightHandSide().accept(this).isIntType() : "Type mismatch";
        return new IntType();
    }

    public Type visit(SubExpr expr) {
        assert expr.getLeftHandSide().accept(this).isIntType()
                && expr.getRightHandSide().accept(this).isIntType() : "Type mismatch";
        return new IntType();
    }

    public Type visit(ArrayAccessExpr expr) {
        assert expr.getArray().accept(this).isIntArrayType() : "Expression does not define an array";
        return new IntType();
    }

    public Type visit(LengthExpr expr) {
        assert expr.getLenExpr().accept(this).isIntArrayType() : "Expression does not define an array";
        return new IntType();
    }

    public Type visit(MethodCallExpr expr) {
        Type method = expr.getMethodNameExpr().accept(this);
        assert method.isMethodType() : "Internal error in DotExpr";
        List<ExprNode> argListForExpr = expr.getArgs();
        List<Pair<String, Variable>> args = ((MethodType) method).getArguments();
        assert argListForExpr.size() == args.size() : "Number of arguments mismatch";
        for (int i = 0; i < argListForExpr.size(); i++) {
            Type argType = argListForExpr.get(i).accept(this);
            assert argType.isVariableType() : "Internal error in MethodCallExpr";

            assert argType.equals(args.get(i).second()) : "Type mismatch in argument call";
        }
        return ((MethodType) method).getReturnType();
    }

    public Type visit(NewArrayDeclExpr expr) {
        return currentClass;
    }

    public Type visit(NewObjectDeclExpr expr) {
        return currentClass;
    }

    public Type visit(NotExpr expr) {
        return currentClass;
    }

}
