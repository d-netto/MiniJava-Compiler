package parser.ast.expression.literals;

import codegen_simple.SimpleCodegenVisitor;
import parser.ast.base_abs_classes.ExprNode;
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

    @Override public String prettyString(String identation) {
        return identation + "IdentifierExpr: " + identifierName;
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

    @Override public void accept(SimpleCodegenVisitor vis) {
        vis.visit(this);
    }

}
