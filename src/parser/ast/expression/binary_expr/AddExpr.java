package parser.ast.expression.binary_expr;

import parser.ast.interfaces.ExprNode;

public class AddExpr extends BinaryExpr {

    public AddExpr(ExprNode leftHandSide, ExprNode rightHandSide) {
        super(leftHandSide, rightHandSide);
    }

}
