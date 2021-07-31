package parser.ast.statement;

import parser.ast.expression.ExprNode;

public class IfStatement implements StatementNode {
    
    private ExprNode ifCondition;
    private StatementNode ifBlock;
    private StatementNode elseBlock;
    
    public IfStatement(ExprNode ifCondition, StatementNode ifBlock, StatementNode elseBlock) {
        this.ifCondition = ifCondition;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

}
