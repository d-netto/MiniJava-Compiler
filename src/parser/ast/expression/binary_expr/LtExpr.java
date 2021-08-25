package parser.ast.expression.binary_expr;

import codegen_simple.SimpleCodegenVisitor;
import parser.ast.base_abs_classes.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class LtExpr extends BinaryExpr {

    public LtExpr(int line, ExprNode leftHandSide, ExprNode rightHandSide) {
        super(line, leftHandSide, rightHandSide);
    }

    @Override public String prettyString(String identation) {
        return identation + "LtExpr:" + "\n" + leftHandSide.prettyString(identation + "\t") + "\n"
                + rightHandSide.prettyString(identation + "\t");
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

    @Override public void accept(SimpleCodegenVisitor vis) {
        vis.visit(this);
    }

}
