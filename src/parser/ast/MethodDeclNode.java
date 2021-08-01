package parser.ast;

import java.util.List;

import parser.MJParser.Pair;
import parser.ast.interfaces.ExprNode;
import parser.ast.interfaces.StatementNode;

public class MethodDeclNode {
    
    private String methodType;
    private String methodName;
    private List<Pair<String, String>> methodArgs;
    private List<VarDeclNode> varDecls;
    private List<StatementNode> statements;
    private ExprNode returnExpr;
    
    public MethodDeclNode(String methodType, String methodName, List<Pair<String, String>> methodArgs, List<VarDeclNode> varDecls, 
            List<StatementNode> statements, ExprNode returnExpr) {
        this.methodType = methodType;
        this.methodName = methodName;
        this.methodArgs = methodArgs;
        this.varDecls = varDecls;
        this.statements = statements;
        this.returnExpr = returnExpr;
    }
    

}
