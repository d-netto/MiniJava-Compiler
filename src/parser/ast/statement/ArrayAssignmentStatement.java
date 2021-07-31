package parser.ast.statement;

import parser.ast.expression.ExprNode;

public class ArrayAssignmentStatement implements StatementNode {
    
    private int lineAssignment;
    private String varAssignedName;
    private ExprNode index;
    private ExprNode rhs;
    
    public ArrayAssignmentStatement(int lineAssignment, String varAssignedName, ExprNode index, ExprNode rhs) {
        this.lineAssignment = lineAssignment;
        this.varAssignedName = varAssignedName;
        this.index = index;
        this.rhs = rhs;
    }

}
