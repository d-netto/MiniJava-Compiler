package parser.ast.expression;

public class ArrayAccessExpr implements ExprNode {
    
    private ExprNode array;
    private ExprNode index;
    
    public ArrayAccessExpr(ExprNode array, ExprNode index) {
        this.array = array;
        this.index = index;
    }

}
