package parser.ast.statement;

import parser.ast.interfaces.ExprNode;
import parser.ast.interfaces.StatementNode;

public class IfStatement implements StatementNode {

    private ExprNode ifCondition;
    private StatementNode ifBlock;
    private StatementNode elseBlock;

    public IfStatement(ExprNode ifCondition, StatementNode ifBlock, StatementNode elseBlock) {
        this.ifCondition = ifCondition;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    public String prettyPrint(String identation) {
        return identation + "IfStatement:" + "\n" + ifCondition.prettyPrint(identation + "\t") + "\n"
                + ifBlock.prettyPrint(identation + "\t") + "\n" + elseBlock.prettyPrint(identation + "\t");
    }

}
