package parser.ast.expression;

import parser.ast.interfaces.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class NewObjectDeclExpr implements ExprNode {

    private final String objName;

    public NewObjectDeclExpr(String objName) {
        this.objName = objName;
    }

    public String prettyPrint(String identation) {
        return identation + "NewObjectDeclExpr:" + "\n" + identation + "\t" + objName;
    }

    public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
