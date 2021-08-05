package parser.ast.expression;

import parser.ast.interfaces.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class ArrayAccessExpr implements ExprNode {

    private final ExprNode array;
    private final ExprNode index;

    public ArrayAccessExpr(ExprNode array, ExprNode index) {
        this.array = array;
        this.index = index;
    }

    public ExprNode getArray() {
        return array;
    }

    public String prettyPrint(String identation) {
        return identation + "ArrayAccessExpr:" + "\n" + array.prettyPrint(identation + "\t") + "\n"
                + index.prettyPrint(identation + "\t");
    }

    public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
