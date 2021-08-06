package parser.ast.statement;

import parser.ast.interfaces.ExprNode;
import parser.ast.interfaces.StatementNode;
import semantics.TypesVisitor;

public class PrintStatement extends StatementNode {

    private final ExprNode printExpr;

    public PrintStatement(int line, ExprNode printExpr) {
        super(line);
        this.printExpr = printExpr;
    }

    public ExprNode getPrintExpr() {
        return printExpr;
    }

    @Override public String prettyPrint(String identation) {
        return identation + "PrintStatement:" + "\n" + printExpr.prettyPrint(identation + "\t");
    }

    public void accept(TypesVisitor vis) {
        vis.visit(this);
    }

}
