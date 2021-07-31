package parser.ast.statement;

import parser.ast.expression.ExprNode;

public class VariableAssignmentStatement implements StatementNode {
    
    private int lineAssignment;
    private String varAssignedName;
    private ExprNode rhs;
    
    public VariableAssignmentStatement(int lineAssignment, String varAssignedName, ExprNode rhs) {
        this.lineAssignment = lineAssignment;
        this.varAssignedName = varAssignedName;
        this.rhs = rhs;
    }

}
