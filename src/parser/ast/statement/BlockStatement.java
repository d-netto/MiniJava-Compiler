package parser.ast.statement;

import java.util.List;

import parser.ast.interfaces.StatementNode;

public class BlockStatement implements StatementNode {
    
    List<StatementNode> statements;
    
    public BlockStatement(List<StatementNode> statements) {
        this.statements = statements;
    }

}
