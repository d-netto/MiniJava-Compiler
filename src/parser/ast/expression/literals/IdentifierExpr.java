package parser.ast.expression.literals;

import parser.ast.interfaces.ExprNode;

public class IdentifierExpr implements ExprNode {
    
    private String identifierName;
    
    public IdentifierExpr(String identifierName) {
        this.identifierName = identifierName;
    }

}
