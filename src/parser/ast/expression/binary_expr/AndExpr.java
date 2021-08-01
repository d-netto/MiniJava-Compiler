package parser.ast.expression.binary_expr;

import parser.ast.interfaces.ExprNode;

public class AndExpr extends BinaryExpr {

    public AndExpr(ExprNode leftHandSide, ExprNode rightHandSide) {
        super(leftHandSide, rightHandSide);
    }

}
