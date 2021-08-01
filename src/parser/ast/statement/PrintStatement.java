package parser.ast.statement;

import parser.ast.interfaces.ExprNode;
import parser.ast.interfaces.StatementNode;

public class PrintStatement implements StatementNode {

    private ExprNode printExpr;

    public PrintStatement(ExprNode printExpr) {
        this.printExpr = printExpr;
    }

    public String prettyPrint(String identation) {
        return identation + "PrintStatement:" + "\n" + printExpr.prettyPrint(identation + "\t");
    }

}
