package parser.ast.statement;

import parser.ast.interfaces.ExprNode;
import parser.ast.interfaces.StatementNode;

public class SetVariableStatement implements StatementNode {
    
    private int lineAssignment;
    private String varAssignedName;
    private ExprNode rhs;
    
    public SetVariableStatement(int lineAssignment, String varAssignedName, ExprNode rhs) {
        this.lineAssignment = lineAssignment;
        this.varAssignedName = varAssignedName;
        this.rhs = rhs;
    }

}
