package parser.ast.interfaces;

import semantics.TypesVisitor;

public abstract class StatementNode {

    private int line;

    public StatementNode(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    public abstract String prettyPrint(String string);

    public abstract void accept(TypesVisitor vis);

}
