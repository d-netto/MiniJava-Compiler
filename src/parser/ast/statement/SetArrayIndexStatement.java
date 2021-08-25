package parser.ast.statement;

import codegen_simple.SimpleCodegenVisitor;
import parser.ast.base_abs_classes.ExprNode;
import parser.ast.base_abs_classes.StatementNode;
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

    @Override public String prettyString(String identation) {
        return identation + "SetArrayIndexStatement:" + "\n" + identation + "\t" + varAssignedName + "\n"
                + index.prettyString(identation + "\t") + "\n" + rightHandSide.prettyString(identation + "\t");
    }

    @Override public void accept(TypesVisitor vis) {
        vis.visit(this);
    }

    @Override public void accept(SimpleCodegenVisitor vis) {
        vis.visit(this);
    }

}
