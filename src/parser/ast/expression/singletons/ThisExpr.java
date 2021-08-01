package parser.ast.expression.singletons;

import parser.ast.interfaces.ExprNode;

public class ThisExpr implements ExprNode {

    public String prettyPrint(String identation) {
        return identation + "ThisExpr";
    }

}
