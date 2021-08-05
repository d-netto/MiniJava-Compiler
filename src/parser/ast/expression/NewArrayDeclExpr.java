package parser.ast.expression;

import parser.ast.interfaces.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class NewArrayDeclExpr implements ExprNode {

    private final ExprNode size;

    public NewArrayDeclExpr(ExprNode size) {
        this.size = size;
    }

    public String prettyPrint(String identation) {
        return identation + "NewArrayDeclExpr:" + "\n" + size.prettyPrint(identation + "\t");
    }

    public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
