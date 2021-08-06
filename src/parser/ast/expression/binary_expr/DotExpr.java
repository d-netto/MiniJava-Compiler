package parser.ast.expression.binary_expr;

import parser.ast.base_abs_classes.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class DotExpr extends BinaryExpr {

    public DotExpr(int line, ExprNode leftHandSide, ExprNode rightHandSide) {
        super(line, leftHandSide, rightHandSide);
    }

    @Override public String prettyString(String identation) {
        return identation + "DotExpr:" + "\n" + leftHandSide.prettyString(identation + "\t") + "\n"
                + rightHandSide.prettyString(identation + "\t");
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
