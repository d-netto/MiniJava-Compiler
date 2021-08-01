package parser.ast.expression.binary_expr;

import parser.ast.interfaces.ExprNode;

public class DotExpr extends BinaryExpr {

    public DotExpr(ExprNode leftHandSide, ExprNode rightHandSide) {
        super(leftHandSide, rightHandSide);
    }

}
