package parser.ast.expression;


public class NewObjectDeclExpr implements ExprNode {

    private String objName;
    
    public NewObjectDeclExpr(String objName) {
        this.objName = objName;
    }
}
