package parser.ast.expression;

import java.util.ArrayList;
import java.util.List;

import parser.ast.interfaces.ExprNode;
import semantics.TypesVisitor;
import semantics.types.Type;

public class MethodCallExpr extends ExprNode {

    private final ExprNode methodNameExpr;
    private final List<ExprNode> args;

    public MethodCallExpr(int line, ExprNode methodNameExpr, List<ExprNode> args) {
        super(line);
        this.methodNameExpr = methodNameExpr;
        this.args = args;
    }

    public ExprNode getMethodNameExpr() {
        return methodNameExpr;
    }

    public List<ExprNode> getArgs() {
        return new ArrayList<>(args);
    }

    @Override public String prettyPrint(String identation) {
        StringBuilder strBuilder = new StringBuilder(
                "MethodCallExpr:" + "\n" + methodNameExpr.prettyPrint(identation + "\t"));
        for (ExprNode arg : args) {
            strBuilder.append("\n" + arg.prettyPrint(identation + "\t"));
        }
        return identation + strBuilder.toString();
    }

    @Override public Type accept(TypesVisitor vis) {
        return vis.visit(this);
    }

}
