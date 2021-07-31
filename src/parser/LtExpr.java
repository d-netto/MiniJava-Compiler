package parser;

import parser.ast.expression.ExprNode;


public class LtExpr implements ExprNode {

    private ExprNode lhs;
    private ExprNode rhs;
    
    public LtExpr(ExprNode lhs, ExprNode rhs){
        this.lhs = lhs;
        this.rhs = rhs;
    }
}
