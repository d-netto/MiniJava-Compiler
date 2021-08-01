package parser.ast.expression.binary_expr;

import parser.ast.interfaces.ExprNode;

public class LtExpr extends BinaryExpr {

    public LtExpr(ExprNode leftHandSide, ExprNode rightHandSide) {
        super(leftHandSide, rightHandSide);
    }

}
