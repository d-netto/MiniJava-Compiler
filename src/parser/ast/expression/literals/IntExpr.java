package parser.ast.expression.literals;

import parser.ast.interfaces.ExprNode;

public class IntExpr implements ExprNode {

    private String integerVal;

    public IntExpr(String integerVal) {
        this.integerVal = integerVal;
    }

    public String prettyPrint(String identation) {
        return identation + "IntExpr: " + integerVal;
    }

}
