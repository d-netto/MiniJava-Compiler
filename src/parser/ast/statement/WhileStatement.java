package parser.ast.statement;

import parser.ast.expression.ExprNode;

public class WhileStatement implements StatementNode {
    
    ExprNode whileCondition;
    StatementNode whileBlock;
    
    public WhileStatement(ExprNode whileCondition, StatementNode whileBlock) {
        this.whileCondition = whileCondition;
        this.whileBlock = whileBlock;
    }

}
