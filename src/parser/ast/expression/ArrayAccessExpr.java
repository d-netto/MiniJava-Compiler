package parser.ast.expression;

import parser.ast.interfaces.ExprNode;

public class ArrayAccessExpr implements ExprNode {

    public final ExprNode array;
    public final ExprNode index;

    public ArrayAccessExpr(ExprNode array, ExprNode index) {
        this.array = array;
        this.index = index;
    }

    public String prettyPrint(String identation) {
        return identation + "ArrayAccessExpr:" + "\n" + array.prettyPrint(identation + "\t") + "\n"
                + index.prettyPrint(identation + "\t");
    }

}
