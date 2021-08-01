package parser.ast.statement;

import parser.ast.interfaces.ExprNode;
import parser.ast.interfaces.StatementNode;

public class WhileStatement implements StatementNode {

    ExprNode whileCondition;
    StatementNode whileBlock;

    public WhileStatement(ExprNode whileCondition, StatementNode whileBlock) {
        this.whileCondition = whileCondition;
        this.whileBlock = whileBlock;
    }

    public String prettyPrint(String identation) {
        return identation + "WhileStatement:" + "\n" + whileCondition.prettyPrint(identation + "\t") + "\n"
                + whileBlock.prettyPrint(identation + "\t");
    }

}
