package parser.ast.statement;

import parser.ast.expression.ExprNode;

public class PrintStatement implements StatementNode {
    
    private ExprNode printExpr;
    
    public PrintStatement(ExprNode printExpr) {
        this.printExpr = printExpr;
    }

}
