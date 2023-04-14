package parser.ast.statement;

import codegen_simple.SimpleCodegenVisitor;
import parser.ast.base_abs_classes.ExprNode;
import parser.ast.base_abs_classes.StatementNode;
import semantics.TypesVisitor;

public class WhileStatement extends StatementNode {

    private ExprNode whileCondition;
    private StatementNode whileBlock;

    public WhileStatement(int line, ExprNode whileCondition, StatementNode whileBlock) {
        super(line);
        this.whileCondition = whileCondition;
        this.whileBlock = whileBlock;
    }

    public ExprNode getWhileCondition() {
        return whileCondition;
    }

    public StatementNode getWhileBlock() {
        return whileBlock;
    }

    @Override public String prettyString(String identation) {
        return identation + "WhileStatement:" + "\n" + whileCondition.prettyString(identation + "\t") + "\n"
                + whileBlock.prettyString(identation + "\t");
    }

    @Override public void accept(TypesVisitor vis) {
        vis.visit(this);
    }

    @Override public void accept(SimpleCodegenVisitor vis) {
        vis.visit(this);
    }

}
