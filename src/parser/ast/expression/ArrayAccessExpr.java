package parser.ast.expression;

import codegen_simple.SimpleCodegenVisitor;
import parser.ast.base_abs_classes.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class ArrayAccessExpr extends ExprNode {

    private final ExprNode array;
    private final ExprNode index;

    public ArrayAccessExpr(int line, ExprNode array, ExprNode index) {
        super(line);
        this.array = array;
        this.index = index;
    }

    public ExprNode getArray() {
        return array;
    }

    public ExprNode getIndex() {
        return index;
    }

    @Override public String prettyString(String identation) {
        return identation + "ArrayAccessExpr:" + "\n" + array.prettyString(identation + "\t") + "\n"
                + index.prettyString(identation + "\t");
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

    @Override public void accept(SimpleCodegenVisitor vis) {
        vis.visit(this);
    }

}
