package parser.ast.expression;


public class AddExpr implements ExprNode {
    
    private ExprNode lhs;
    private ExprNode rhs;
    
    public AddExpr(ExprNode lhs, ExprNode rhs){
        this.lhs = lhs;
        this.rhs = rhs;
    }

}
