package parser.ast.statement;

import parser.ast.interfaces.ExprNode;
import parser.ast.interfaces.StatementNode;
import semantics.TypesVisitor;

public class WhileStatement extends StatementNode {

    private ExprNode whileCondition;
    private StatementNode whileBlock;

    public WhileStatement(int line, ExprNode whileCondition, StatementNode whileBlock) {
        super(line);
        this.whileCondition = whileCondition;
        this.whileBlock = whileBlock;
    }

    public ExprNode getWhileCondition() {
        return whileCondition;
    }

    public StatementNode getWhileBlock() {
        return whileBlock;
    }

    @Override public String prettyPrint(String identation) {
        return identation + "WhileStatement:" + "\n" + whileCondition.prettyPrint(identation + "\t") + "\n"
                + whileBlock.prettyPrint(identation + "\t");
    }

    public void accept(TypesVisitor vis) {
        vis.visit(this);
    }

}
