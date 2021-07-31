package parser.ast.expression;


public class DotExpr implements ExprNode {
    
    private ExprNode lhs;
    private ExprNode rhs;
    
    public DotExpr(ExprNode lhs, ExprNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

}
