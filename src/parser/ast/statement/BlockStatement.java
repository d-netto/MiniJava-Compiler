package parser.ast.statement;

import java.util.ArrayList;
import java.util.List;

import parser.ast.interfaces.StatementNode;
import semantics.TypesVisitor;

public class BlockStatement extends StatementNode {

    private List<StatementNode> statements;

    public BlockStatement(int line, List<StatementNode> statements) {
        super(line);
        this.statements = statements;
    }

    public List<StatementNode> getStatements() {
        return new ArrayList<>(statements);
    }

    @Override public String prettyPrint(String identation) {
        StringBuilder strBuilder = new StringBuilder("BlockStatement:");
        for (StatementNode statement : statements) {
            strBuilder.append(statement.prettyPrint(identation + "\t"));
        }
        return identation + strBuilder.toString();
    }

    public void accept(TypesVisitor vis) {
        vis.visit(this);
    }

}
