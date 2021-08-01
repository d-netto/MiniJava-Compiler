package parser.ast.expression;

import parser.ast.interfaces.ExprNode;

public class IntExpr implements ExprNode {
    
    private String integerVal;
    
    public IntExpr(String integerVal) {
        this.integerVal = integerVal;
    }

}
