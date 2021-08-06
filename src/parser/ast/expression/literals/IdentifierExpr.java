package parser.ast.expression.literals;

import parser.ast.interfaces.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class IdentifierExpr extends ExprNode {

    private final String identifierName;

    public IdentifierExpr(int line, String identifierName) {
        super(line);
        this.identifierName = identifierName;
    }

    public String getIdentifierName() {
        return identifierName;
    }

    @Override public String prettyPrint(String identation) {
        return identation + "IdentifierExpr: " + identifierName;
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
