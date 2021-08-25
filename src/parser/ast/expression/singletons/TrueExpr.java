package parser.ast.expression.singletons;

import codegen_simple.SimpleCodegenVisitor;
import parser.ast.base_abs_classes.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class TrueExpr extends ExprNode {

    public TrueExpr(int line) {
        super(line);
    }

    @Override public String prettyString(String identation) {
        return identation + "TrueExpr";
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

    @Override public void accept(SimpleCodegenVisitor vis) {
        vis.visit(this);
    }

}
