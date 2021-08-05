package parser.ast.expression;

import java.util.ArrayList;
import java.util.List;

import parser.ast.interfaces.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class MethodCallExpr implements ExprNode {

    private final ExprNode methodNameExpr;
    private final List<ExprNode> args;

    public MethodCallExpr(ExprNode methodNameExpr, List<ExprNode> args) {
        this.methodNameExpr = methodNameExpr;
        this.args = args;
    }

    public ExprNode getMethodNameExpr() {
        return methodNameExpr;
    }

    public List<ExprNode> getArgs() {
        return new ArrayList<>(args);
    }

    public String prettyPrint(String identation) {
        String str = "MethodCallExpr:" + "\n" + methodNameExpr.prettyPrint(identation + "\t");
        for (ExprNode arg : args) {
            str += "\n" + arg.prettyPrint(identation + "\t");
        }
        return identation + str;
    }

    public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
