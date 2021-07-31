package parser.ast.expression;


public class NotExpr implements ExprNode {
    
    private ExprNode arg;
    
    public NotExpr(ExprNode arg) {
        this.arg = arg;
    }

}
