package parser.ast.expression;

import codegen_simple.SimpleCodegenVisitor;
import parser.ast.base_abs_classes.ExprNode;
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

    @Override public String prettyString(String identation) {
        return identation + "LengthExpr:" + "\n" + lenExpr.prettyString(identation + "\t");
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

    @Override public void accept(SimpleCodegenVisitor vis) {
        vis.visit(this);
    }

}
