package parser.ast.expression;

import parser.ast.interfaces.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class NotExpr extends ExprNode {

    private final ExprNode argument;

    public NotExpr(int line, ExprNode argument) {
        super(line);
        this.argument = argument;
    }

    public ExprNode getArgument() {
        return argument;
    }

    @Override public String prettyPrint(String identation) {
        return identation + "NotExpr:" + "\n" + argument.prettyPrint(identation + "\t");
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
