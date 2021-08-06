package parser.ast.expression;

import parser.ast.interfaces.ExprNode;
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

    @Override public String prettyPrint(String identation) {
        return identation + "NewObjectDeclExpr:" + "\n" + identation + "\t" + objName;
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
