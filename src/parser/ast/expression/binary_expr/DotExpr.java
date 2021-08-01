package parser.ast.expression.binary_expr;

import parser.ast.interfaces.ExprNode;

public class DotExpr extends BinaryExpr {

    public DotExpr(ExprNode leftHandSide, ExprNode rightHandSide) {
        super(leftHandSide, rightHandSide);
    }

    public String prettyPrint(String identation) {
        return identation + "DotExpr:" + "\n" + leftHandSide.prettyPrint(identation + "\t") + "\n"
                + rightHandSide.prettyPrint(identation + "\t");
    }

}
