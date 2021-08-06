package parser.ast.expression.singletons;

import parser.ast.interfaces.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class TrueExpr extends ExprNode {

    public TrueExpr(int line) {
        super(line);
    }

    @Override public String prettyPrint(String identation) {
        return identation + "TrueExpr";
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
