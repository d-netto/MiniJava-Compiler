package parser.ast.statement;

import java.util.ArrayList;
import java.util.List;

import parser.ast.interfaces.StatementNode;
import semantics.TypesVisitor;

public class BlockStatement implements StatementNode {

    private List<StatementNode> statements;

    public BlockStatement(List<StatementNode> statements) {
        this.statements = statements;
    }

    public List<StatementNode> getStatements() {
        return new ArrayList<>(statements);
    }

    public String prettyPrint(String identation) {
        String str = "BlockStatement:";
        for (StatementNode statement : statements) {
            str += statement.prettyPrint(identation + "\t");
        }
        return identation + str;
    }

    public void accept(TypesVisitor vis) {
        vis.visit(this);
    }

}
