package parser.ast.statement;

import codegen_simple.SimpleCodegenVisitor;
import parser.ast.base_abs_classes.ExprNode;
import parser.ast.base_abs_classes.StatementNode;
import semantics.TypesVisitor;

public class IfStatement extends StatementNode {

    private final ExprNode ifCondition;
    private final StatementNode ifBlock;
    private final StatementNode elseBlock;

    public IfStatement(int line, ExprNode ifCondition, StatementNode ifBlock, StatementNode elseBlock) {
        super(line);
        this.ifCondition = ifCondition;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    public ExprNode getIfCondition() {
        return ifCondition;
    }

    public StatementNode getIfBlock() {
        return ifBlock;
    }

    public StatementNode getElseBlock() {
        return elseBlock;
    }

    @Override public String prettyString(String identation) {
        return identation + "IfStatement:" + "\n" + ifCondition.prettyString(identation + "\t") + "\n"
                + ifBlock.prettyString(identation + "\t") + "\n" + elseBlock.prettyString(identation + "\t");
    }

    @Override public void accept(TypesVisitor vis) {
        vis.visit(this);
    }

    @Override public void accept(SimpleCodegenVisitor vis) {
        vis.visit(this);
    }

}
