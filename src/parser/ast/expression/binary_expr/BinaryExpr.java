package parser.ast.expression.binary_expr;

import parser.ast.interfaces.ExprNode;

public abstract class BinaryExpr implements ExprNode {

    private ExprNode leftHandSide;
    private ExprNode rightHandSide;

    public BinaryExpr(ExprNode leftHandSide, ExprNode rightHandSide) {
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
    }

}
