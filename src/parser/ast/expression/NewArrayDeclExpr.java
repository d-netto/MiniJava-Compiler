package parser.ast.expression;

public class NewArrayDeclExpr implements ExprNode {
    
    private ExprNode size;
    
    public NewArrayDeclExpr(ExprNode size) {
        this.size = size;
    }

}
