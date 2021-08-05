package parser.ast.expression.binary_expr;

import parser.ast.interfaces.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class AndExpr extends BinaryExpr {

    public AndExpr(ExprNode leftHandSide, ExprNode rightHandSide) {
        super(leftHandSide, rightHandSide);
    }

    public String prettyPrint(String identation) {
        return identation + "AndExpr:" + "\n" + leftHandSide.prettyPrint(identation + "\t") + "\n"
                + rightHandSide.prettyPrint(identation + "\t");
    }

    public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
