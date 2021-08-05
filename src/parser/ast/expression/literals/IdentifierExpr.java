package parser.ast.expression.literals;

import parser.ast.interfaces.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class IdentifierExpr implements ExprNode {

    private final String identifierName;

    public IdentifierExpr(String identifierName) {
        this.identifierName = identifierName;
    }

    public String getIdentifierName() {
        return identifierName;
    }

    public String prettyPrint(String identation) {
        return identation + "IdentifierExpr: " + identifierName;
    }

    public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
