package parser.ast.expression.literals;

import parser.ast.interfaces.ExprNode;

public class LengthExpr implements ExprNode {
    
    private ExprNode lenExpr;
    
    public LengthExpr(ExprNode lenExpr) {
        this.lenExpr = lenExpr;
    }

}
