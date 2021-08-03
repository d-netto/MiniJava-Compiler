package parser.ast.expression;

import parser.ast.interfaces.ExprNode;

public class NotExpr implements ExprNode {

    public final ExprNode argument;

    public NotExpr(ExprNode argument) {
        this.argument = argument;
    }

    public String prettyPrint(String identation) {
        return identation + "NotExpr:" + "\n" + argument.prettyPrint(identation + "\t");
    }

}
