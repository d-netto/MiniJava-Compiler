package parser.ast.statement;

import parser.ast.interfaces.ExprNode;
import parser.ast.interfaces.StatementNode;

public class SetVariableStatement implements StatementNode {

    private int lineAssignment;
    private String varAssignedName;
    private ExprNode rightHandSide;

    public SetVariableStatement(int lineAssignment, String varAssignedName, ExprNode rightHandSide) {
        this.lineAssignment = lineAssignment;
        this.varAssignedName = varAssignedName;
        this.rightHandSide = rightHandSide;
    }

    public String prettyPrint(String identation) {
        return identation + "SetVariableStatement:" + "\n" + identation + "\t" + varAssignedName + "\n"
                + rightHandSide.prettyPrint(identation + "\t");
    }

}
