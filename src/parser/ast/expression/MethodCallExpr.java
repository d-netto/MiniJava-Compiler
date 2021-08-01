package parser.ast.expression;

import java.util.List;

import parser.ast.interfaces.ExprNode;

public class MethodCallExpr implements ExprNode {

    private ExprNode methodNameExpr;
    private List<ExprNode> args;

    public MethodCallExpr(ExprNode methodNameExpr, List<ExprNode> args) {
        this.methodNameExpr = methodNameExpr;
        this.args = args;
    }

    public String prettyPrint(String identation) {
        String str = "MethodCallExpr:" + "\n" + methodNameExpr.prettyPrint(identation + "\t");
        for (ExprNode arg : args) {
            str += "\n" + arg.prettyPrint(identation + "\t");
        }
        return identation + str;
    }

}
