package parser.ast.expression.literals;

import parser.ast.interfaces.ExprNode;

public class IntExpr implements ExprNode {

    public final String integerVal;

    public IntExpr(String integerVal) {
        this.integerVal = integerVal;
    }

    public String prettyPrint(String identation) {
        return identation + "IntExpr: " + integerVal;
    }

}
