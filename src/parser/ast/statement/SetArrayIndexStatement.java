package parser.ast.statement;

import parser.ast.interfaces.ExprNode;
import parser.ast.interfaces.StatementNode;

public class SetArrayIndexStatement implements StatementNode {

    public final int lineAssignment;
    public final String varAssignedName;
    public final ExprNode index;
    public final ExprNode rightHandSide;

    public SetArrayIndexStatement(int lineAssignment, String varAssignedName, ExprNode index, ExprNode rightHandSide) {
        this.lineAssignment = lineAssignment;
        this.varAssignedName = varAssignedName;
        this.index = index;
        this.rightHandSide = rightHandSide;
    }

    public String prettyPrint(String identation) {
        return identation + "SetArrayIndexStatement:" + "\n" + identation + "\t" + varAssignedName + "\n"
                + index.prettyPrint(identation + "\t") + "\n" + rightHandSide.prettyPrint(identation + "\t");
    }

}
