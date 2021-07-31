package parser.ast.expression;


public class AndExpr implements ExprNode {
    
    private ExprNode lhs;
    private ExprNode rhs;
    
    public AndExpr(ExprNode lhs, ExprNode rhs){
        this.lhs = lhs;
        this.rhs = rhs;
    }

}
