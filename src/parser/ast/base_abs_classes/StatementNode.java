package parser.ast.base_abs_classes;

import codegen_simple.SimpleCodegenVisitor;
import semantics.TypesVisitor;

public abstract class StatementNode {

    private int line;

    public StatementNode(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    public abstract String prettyString(String string);

    public abstract void accept(TypesVisitor vis);

    public abstract void accept(SimpleCodegenVisitor vis);

}
