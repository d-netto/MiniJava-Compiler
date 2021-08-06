package parser.ast.statement;

import parser.ast.interfaces.ExprNode;
import parser.ast.interfaces.StatementNode;
import semantics.TypesVisitor;

public class SetArrayIndexStatement extends StatementNode {

    private final String varAssignedName;
    private final ExprNode index;
    private final ExprNode rightHandSide;

    public SetArrayIndexStatement(int line, String varAssignedName, ExprNode index, ExprNode rightHandSide) {
        super(line);
        this.varAssignedName = varAssignedName;
        this.index = index;
        this.rightHandSide = rightHandSide;
    }

    public String getVarAssignedName() {
        return varAssignedName;
    }

    public ExprNode getIndex() {
        return index;
    }

    public ExprNode getRightHandSide() {
        return rightHandSide;
    }

    @Override public String prettyPrint(String identation) {
        return identation + "SetArrayIndexStatement:" + "\n" + identation + "\t" + varAssignedName + "\n"
                + index.prettyPrint(identation + "\t") + "\n" + rightHandSide.prettyPrint(identation + "\t");
    }

    public void accept(TypesVisitor vis) {
        vis.visit(this);
    }

}
