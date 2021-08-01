package parser.ast.expression.binary_expr;

import parser.ast.interfaces.ExprNode;

public class AddExpr extends BinaryExpr {

    public AddExpr(ExprNode leftHandSide, ExprNode rightHandSide) {
        super(leftHandSide, rightHandSide);
    }

    public String prettyPrint(String identation) {
        return identation + "AddExpr:" + "\n" + leftHandSide.prettyPrint(identation + "\t") + "\n"
                + rightHandSide.prettyPrint(identation + "\t");
    }

}
