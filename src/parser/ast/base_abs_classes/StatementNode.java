package parser.ast.base_abs_classes;

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

}
