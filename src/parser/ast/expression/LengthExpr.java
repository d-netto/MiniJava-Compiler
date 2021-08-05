package parser.ast.expression;

import parser.ast.interfaces.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class LengthExpr implements ExprNode {

    private final ExprNode lenExpr;

    public LengthExpr(ExprNode lenExpr) {
        this.lenExpr = lenExpr;
    }

    public ExprNode getLenExpr() {
        return lenExpr;
    }

    public String prettyPrint(String identation) {
        return identation + "LengthExpr:" + "\n" + lenExpr.prettyPrint(identation + "\t");
    }

    public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
