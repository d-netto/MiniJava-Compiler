package parser.ast.expression;

import parser.ast.interfaces.ExprNode;

public class NewArrayDeclExpr implements ExprNode {
    
    private ExprNode size;
    
    public NewArrayDeclExpr(ExprNode size) {
        this.size = size;
    }

}
