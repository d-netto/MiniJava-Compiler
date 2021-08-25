package parser.ast.base_abs_classes;

import codegen_simple.SimpleCodegenVisitor;
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

    public abstract String prettyString(String string);

    public abstract Type accept(TypesVisitor vis);

    public abstract void accept(SimpleCodegenVisitor vis);

}
