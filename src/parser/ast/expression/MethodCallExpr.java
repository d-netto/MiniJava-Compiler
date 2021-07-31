package parser.ast.expression;

import java.util.List;

public class MethodCallExpr implements ExprNode {
    
    private ExprNode methodNameExpr;
    private List<ExprNode> args;
    
    public MethodCallExpr(ExprNode methodNameExpr, List<ExprNode> args) {
        this.methodNameExpr = methodNameExpr;
        this.args = args;
    }

}
