package parser.ast.expression;

import parser.ast.interfaces.ExprNode;

public class NewArrayDeclExpr implements ExprNode {

    public final ExprNode size;

    public NewArrayDeclExpr(ExprNode size) {
        this.size = size;
    }

    public String prettyPrint(String identation) {
        return identation + "NewArrayDeclExpr:" + "\n" + size.prettyPrint(identation + "\t");
    }

}
