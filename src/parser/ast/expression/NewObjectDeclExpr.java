package parser.ast.expression;

import parser.ast.interfaces.ExprNode;

public class NewObjectDeclExpr implements ExprNode {

    public final String objName;

    public NewObjectDeclExpr(String objName) {
        this.objName = objName;
    }

    public String prettyPrint(String identation) {
        return identation + "NewObjectDeclExpr:" + "\n" + identation + "\t" + objName;
    }

}
