package parser.ast.expression.singletons;

import parser.ast.interfaces.ExprNode;

public class NotExpr implements ExprNode {
    
    private ExprNode arg;
    
    public NotExpr(ExprNode arg) {
        this.arg = arg;
    }

}
