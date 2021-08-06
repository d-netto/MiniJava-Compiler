package parser.ast.statement;

import parser.ast.base_abs_classes.ExprNode;
import parser.ast.base_abs_classes.StatementNode;
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

    @Override public String prettyString(String identation) {
        return identation + "PrintStatement:" + "\n" + printExpr.prettyString(identation + "\t");
    }

    public void accept(TypesVisitor vis) {
        vis.visit(this);
    }

}
