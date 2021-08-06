package parser.ast.statement;

import java.util.ArrayList;
import java.util.List;

import parser.ast.base_abs_classes.StatementNode;
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

    @Override public String prettyString(String identation) {
        StringBuilder strBuilder = new StringBuilder("BlockStatement:");
        for (StatementNode statement : statements) {
            strBuilder.append(statement.prettyString(identation + "\t"));
        }
        return identation + strBuilder.toString();
    }

    public void accept(TypesVisitor vis) {
        vis.visit(this);
    }

}
