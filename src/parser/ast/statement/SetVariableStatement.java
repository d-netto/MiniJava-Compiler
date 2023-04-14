package parser.ast.statement;

import codegen_simple.SimpleCodegenVisitor;
import parser.ast.base_abs_classes.ExprNode;
import parser.ast.base_abs_classes.StatementNode;
import parser.ast.expression.literals.IdentifierExpr;
import semantics.TypesVisitor;

public class SetVariableStatement extends StatementNode {

    private final IdentifierExpr varAssigned;
    private final ExprNode rightHandSide;

    public SetVariableStatement(int line, IdentifierExpr varAssigned, ExprNode rightHandSide) {
        super(line);
        this.varAssigned = varAssigned;
        this.rightHandSide = rightHandSide;
    }

    public IdentifierExpr getVarAssigned() {
        return varAssigned;
    }

    public ExprNode getRightHandSide() {
        return rightHandSide;
    }

    @Override public String prettyString(String identation) {
        return identation + "SetVariableStatement:" + "\n" + identation + "\t"
                + varAssigned.prettyString(identation + "\t") + "\n" + rightHandSide.prettyString(identation + "\t");
    }

    @Override public void accept(TypesVisitor vis) {
        vis.visit(this);
    }

    @Override public void accept(SimpleCodegenVisitor vis) {
        vis.visit(this);
    }

}
