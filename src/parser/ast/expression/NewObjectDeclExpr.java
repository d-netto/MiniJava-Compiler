package parser.ast.expression;

import codegen_simple.SimpleCodegenVisitor;
import parser.ast.base_abs_classes.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class NewObjectDeclExpr extends ExprNode {

    private final String objName;

    public NewObjectDeclExpr(int line, String objName) {
        super(line);
        this.objName = objName;
    }

    public String getObjectName() {
        return objName;
    }

    @Override public String prettyString(String identation) {
        return identation + "NewObjectDeclExpr:" + "\n" + identation + "\t" + objName;
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

    @Override public void accept(SimpleCodegenVisitor vis) {
        vis.visit(this);
    }

}
