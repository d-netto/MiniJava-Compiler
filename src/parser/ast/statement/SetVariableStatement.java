package parser.ast.statement;

import parser.ast.base_abs_classes.ExprNode;
import parser.ast.base_abs_classes.StatementNode;
import semantics.TypesVisitor;

public class SetVariableStatement extends StatementNode {

    private final String varAssignedName;
    private final ExprNode rightHandSide;

    public SetVariableStatement(int line, String varAssignedName, ExprNode rightHandSide) {
        super(line);
        this.varAssignedName = varAssignedName;
        this.rightHandSide = rightHandSide;
    }

    public String getVarAssignedName() {
        return varAssignedName;
    }

    public ExprNode getRightHandSide() {
        return rightHandSide;
    }

    @Override public String prettyString(String identation) {
        return identation + "SetVariableStatement:" + "\n" + identation + "\t" + varAssignedName + "\n"
                + rightHandSide.prettyString(identation + "\t");
    }

    public void accept(TypesVisitor vis) {
        vis.visit(this);
    }

}
