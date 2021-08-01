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

}
