package parser.ast.expression.singletons;

import parser.ast.interfaces.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class TrueExpr implements ExprNode {

    public String prettyPrint(String identation) {
        return identation + "TrueExpr";
    }

    public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
