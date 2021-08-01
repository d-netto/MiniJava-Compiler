package parser.ast.expression.binary_expr;

import parser.ast.interfaces.ExprNode;

public class MultExpr extends BinaryExpr {

    public MultExpr(ExprNode leftHandSide, ExprNode rightHandSide) {
        super(leftHandSide, rightHandSide);
    }

}
