package parser.ast.statement;

import parser.ast.interfaces.ExprNode;
import parser.ast.interfaces.StatementNode;

public class SetArrayIndexStatement implements StatementNode {
    
    private int lineAssignment;
    private String varAssignedName;
    private ExprNode index;
    private ExprNode rhs;
    
    public SetArrayIndexStatement(int lineAssignment, String varAssignedName, ExprNode index, ExprNode rhs) {
        this.lineAssignment = lineAssignment;
        this.varAssignedName = varAssignedName;
        this.index = index;
        this.rhs = rhs;
    }

}
