package parser.ast.expression.literals;

import codegen_simple.SimpleCodegenVisitor;
import parser.ast.base_abs_classes.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class IntExpr extends ExprNode {

    private final String integerVal;

    public IntExpr(int line, String integerVal) {
        super(line);
        this.integerVal = integerVal;
    }

    public String getIntegerVal() {
        return integerVal;
    }

    @Override public String prettyString(String identation) {
        return identation + "IntExpr: " + integerVal;
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

    @Override public void accept(SimpleCodegenVisitor vis) {
        vis.visit(this);
    }

}
