package parser.ast.expression.binary_expr;

import parser.ast.interfaces.ExprNode;

public abstract class BinaryExpr implements ExprNode {

    protected ExprNode leftHandSide;
    protected ExprNode rightHandSide;

    public BinaryExpr(ExprNode leftHandSide, ExprNode rightHandSide) {
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
    }

    public ExprNode getLeftHandSide() {
        return leftHandSide;
    }

    public ExprNode getRightHandSide() {
        return rightHandSide;
    }

}
