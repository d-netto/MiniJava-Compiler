package parser.ast.expression.singletons;

import parser.ast.base_abs_classes.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class ThisExpr extends ExprNode {

    public ThisExpr(int line) {
        super(line);
    }

    @Override public String prettyString(String identation) {
        return identation + "ThisExpr";
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
