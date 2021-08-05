package parser.ast.statement;

import parser.ast.interfaces.ExprNode;
import parser.ast.interfaces.StatementNode;
import semantics.TypesVisitor;

public class WhileStatement implements StatementNode {

    private ExprNode whileCondition;
    private StatementNode whileBlock;

    public WhileStatement(ExprNode whileCondition, StatementNode whileBlock) {
        this.whileCondition = whileCondition;
        this.whileBlock = whileBlock;
    }

    public ExprNode getWhileCondition() {
        return whileCondition;
    }

    public StatementNode getWhileBlock() {
        return whileBlock;
    }

    public String prettyPrint(String identation) {
        return identation + "WhileStatement:" + "\n" + whileCondition.prettyPrint(identation + "\t") + "\n"
                + whileBlock.prettyPrint(identation + "\t");
    }

    public void accept(TypesVisitor vis) {
        vis.visit(this);
    }

}
