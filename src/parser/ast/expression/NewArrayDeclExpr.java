package parser.ast.expression;

import parser.ast.interfaces.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class NewArrayDeclExpr extends ExprNode {

    private final ExprNode size;

    public NewArrayDeclExpr(int line, ExprNode size) {
        super(line);
        this.size = size;
    }

    @Override public String prettyPrint(String identation) {
        return identation + "NewArrayDeclExpr:" + "\n" + size.prettyPrint(identation + "\t");
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
