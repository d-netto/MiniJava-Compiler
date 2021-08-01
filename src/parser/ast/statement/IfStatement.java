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

}
