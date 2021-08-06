package parser.ast.expression.singletons;

import parser.ast.interfaces.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class FalseExpr extends ExprNode {

    public FalseExpr(int line) {
        super(line);
    }

    @Override public String prettyPrint(String identation) {
        return identation + "FalseExpr";
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
