package parser.ast.statement;

import java.util.List;

import parser.ast.interfaces.StatementNode;

public class BlockStatement implements StatementNode {

    List<StatementNode> statements;

    public BlockStatement(List<StatementNode> statements) {
        this.statements = statements;
    }

    public String prettyPrint(String identation) {
        String str = "BlockStatement:";
        for (StatementNode statement : statements) {
            str += statement.prettyPrint(identation + "\t");
        }
        return identation + str;
    }

}
