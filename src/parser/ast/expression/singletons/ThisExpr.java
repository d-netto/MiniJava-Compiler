package parser.ast.expression.singletons;

import parser.ast.interfaces.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class ThisExpr implements ExprNode {

    public String prettyPrint(String identation) {
        return identation + "ThisExpr";
    }

    public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
