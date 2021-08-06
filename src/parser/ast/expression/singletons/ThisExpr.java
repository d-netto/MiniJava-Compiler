package parser.ast.expression.singletons;

import parser.ast.interfaces.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class ThisExpr extends ExprNode {

    public ThisExpr(int line) {
        super(line);
    }

    @Override public String prettyPrint(String identation) {
        return identation + "ThisExpr";
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
