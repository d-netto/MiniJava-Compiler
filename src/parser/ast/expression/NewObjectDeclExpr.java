package parser.ast.expression;

import parser.ast.interfaces.ExprNode;

public class NewObjectDeclExpr implements ExprNode {

    private String objName;
    
    public NewObjectDeclExpr(String objName) {
        this.objName = objName;
    }
}
