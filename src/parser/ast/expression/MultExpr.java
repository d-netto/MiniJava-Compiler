package parser.ast.expression;


public class MultExpr implements ExprNode {
    
    private ExprNode lhs;
    private ExprNode rhs;
    
    public MultExpr(ExprNode lhs, ExprNode rhs){
        this.lhs = lhs;
        this.rhs = rhs;
    }

}
