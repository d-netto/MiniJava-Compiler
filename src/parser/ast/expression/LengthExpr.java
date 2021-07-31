package parser.ast.expression;


public class LengthExpr implements ExprNode {
    
    private ExprNode lenExpr;
    
    public LengthExpr(ExprNode lenExpr) {
        this.lenExpr = lenExpr;
    }

}
