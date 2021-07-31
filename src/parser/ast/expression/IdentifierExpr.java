package parser.ast.expression;


public class IdentifierExpr implements ExprNode {
    
    private String identifierName;
    
    public IdentifierExpr(String identifierName) {
        this.identifierName = identifierName;
    }

}
