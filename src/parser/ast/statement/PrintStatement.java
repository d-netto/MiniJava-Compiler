package parser.ast.statement;

import parser.ast.interfaces.ExprNode;
import parser.ast.interfaces.StatementNode;
import semantics.TypesVisitor;

public class PrintStatement implements StatementNode {

    private final ExprNode printExpr;

    public PrintStatement(ExprNode printExpr) {
        this.printExpr = printExpr;
    }

    public ExprNode getPrintExpr() {
        return printExpr;
    }

    public String prettyPrint(String identation) {
        return identation + "PrintStatement:" + "\n" + printExpr.prettyPrint(identation + "\t");
    }

    public void accept(TypesVisitor vis) {
        vis.visit(this);
    }

}
