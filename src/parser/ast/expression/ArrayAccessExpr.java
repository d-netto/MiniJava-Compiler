package parser.ast.expression;

import parser.ast.interfaces.ExprNode;
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

    @Override public String prettyPrint(String identation) {
        return identation + "ArrayAccessExpr:" + "\n" + array.prettyPrint(identation + "\t") + "\n"
                + index.prettyPrint(identation + "\t");
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
