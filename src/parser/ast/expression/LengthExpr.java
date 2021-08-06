package parser.ast.expression;

import parser.ast.interfaces.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class LengthExpr extends ExprNode {

    private final ExprNode lenExpr;

    public LengthExpr(int line, ExprNode lenExpr) {
        super(line);
        this.lenExpr = lenExpr;
    }

    public ExprNode getLenExpr() {
        return lenExpr;
    }

    @Override public String prettyPrint(String identation) {
        return identation + "LengthExpr:" + "\n" + lenExpr.prettyPrint(identation + "\t");
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
