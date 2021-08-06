package parser.ast.expression.binary_expr;

import parser.ast.base_abs_classes.ExprNode;

public abstract class BinaryExpr extends ExprNode {

    protected ExprNode leftHandSide;
    protected ExprNode rightHandSide;

    public BinaryExpr(int line, ExprNode leftHandSide, ExprNode rightHandSide) {
        super(line);
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
