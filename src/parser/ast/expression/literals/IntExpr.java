package parser.ast.expression.literals;

import parser.ast.interfaces.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class IntExpr implements ExprNode {

    private final String integerVal;

    public IntExpr(String integerVal) {
        this.integerVal = integerVal;
    }

    public String prettyPrint(String identation) {
        return identation + "IntExpr: " + integerVal;
    }

    public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
