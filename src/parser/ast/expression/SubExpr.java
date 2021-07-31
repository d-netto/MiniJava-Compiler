package parser.ast.expression;


public class SubExpr implements ExprNode {
    
    private ExprNode lhs;
    private ExprNode rhs;
    
    public SubExpr(ExprNode lhs, ExprNode rhs){
        this.lhs = lhs;
        this.rhs = rhs;
    }

}
