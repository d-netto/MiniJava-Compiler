package parser.ast.expression;

import parser.ast.interfaces.ExprNode;

public class ArrayAccessExpr implements ExprNode {

    private ExprNode array;
    private ExprNode index;

    public ArrayAccessExpr(ExprNode array, ExprNode index) {
        this.array = array;
        this.index = index;
    }

    public String prettyPrint(String identation) {
        return identation + "ArrayAccessExpr:" + "\n" + array.prettyPrint(identation + "\t") + "\n"
                + index.prettyPrint(identation + "\t");
    }

}
