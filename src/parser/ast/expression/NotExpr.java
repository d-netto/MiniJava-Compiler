package parser.ast.expression;

import codegen_simple.SimpleCodegenVisitor;
import parser.ast.base_abs_classes.ExprNode;
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

    @Override public String prettyString(String identation) {
        return identation + "NotExpr:" + "\n" + argument.prettyString(identation + "\t");
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

    @Override public void accept(SimpleCodegenVisitor vis) {
        vis.visit(this);
    }

}
