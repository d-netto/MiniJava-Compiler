package parser.ast.expression.binary_expr;

import codegen_simple.SimpleCodegenVisitor;
import parser.ast.base_abs_classes.ExprNode;
import parser.ast.expression.literals.IdentifierExpr;
import semantics.TypesVisitor;
import semantics.types.Type;

public class DotExpr extends ExprNode {

    private ExprNode leftHandSide;
    private IdentifierExpr rightHandSide;

    public DotExpr(int line, ExprNode leftHandSide, IdentifierExpr rightHandSide) {
        super(line);
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
    }

    public ExprNode getLeftHandSide() {
        return leftHandSide;
    }

    public IdentifierExpr getRightHandSide() {
        return rightHandSide;
    }

    @Override public String prettyString(String identation) {
        return identation + "DotExpr:" + "\n" + leftHandSide.prettyString(identation + "\t") + "\n"
                + rightHandSide.prettyString(identation + "\t");
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

    @Override public void accept(SimpleCodegenVisitor vis) {
        vis.visit(this);
    }

}
