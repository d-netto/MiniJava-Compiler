package parser.ast.expression;

import codegen_simple.SimpleCodegenVisitor;
import parser.ast.base_abs_classes.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class NewArrayDeclExpr extends ExprNode {

    private final ExprNode size;

    public NewArrayDeclExpr(int line, ExprNode size) {
        super(line);
        this.size = size;
    }

    public ExprNode getSize() {
        return size;
    }

    @Override public String prettyString(String identation) {
        return identation + "NewArrayDeclExpr:" + "\n" + size.prettyString(identation + "\t");
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

    @Override public void accept(SimpleCodegenVisitor vis) {
        vis.visit(this);
    }

}
