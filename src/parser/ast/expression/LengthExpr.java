package parser.ast.expression;

import parser.ast.interfaces.ExprNode;

public class LengthExpr implements ExprNode {

    public final ExprNode lenExpr;

    public LengthExpr(ExprNode lenExpr) {
        this.lenExpr = lenExpr;
    }

    public String prettyPrint(String identation) {
        return identation + "LengthExpr:" + "\n" + lenExpr.prettyPrint(identation + "\t");
    }
}
