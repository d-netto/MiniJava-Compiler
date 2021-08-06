package parser.ast.interfaces;

import semantics.TypesVisitor;
import semantics.types.Type;

public abstract class ExprNode {

    protected int line;

    public ExprNode(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    public abstract String prettyPrint(String string);

    public abstract Type accept(TypesVisitor vis);

}
