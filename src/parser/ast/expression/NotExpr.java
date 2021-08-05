package parser.ast.expression;

import parser.ast.interfaces.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class NotExpr implements ExprNode {

    private final ExprNode argument;

    public NotExpr(ExprNode argument) {
        this.argument = argument;
    }

    public ExprNode getArgument() {
        return argument;
    }

    public String prettyPrint(String identation) {
        return identation + "NotExpr:" + "\n" + argument.prettyPrint(identation + "\t");
    }

    public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
