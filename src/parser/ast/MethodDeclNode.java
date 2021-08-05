package parser.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import parser.ast.interfaces.ExprNode;
import parser.ast.interfaces.StatementNode;
import semantics.BuilderVisitor;
import semantics.types.MethodType;
import utils.Pair;

public class MethodDeclNode {

    private final String methodType;
    private final String methodName;
    private final List<Pair<String, String>> methodArgs;
    private final List<VarDeclNode> varDecls;
    private final List<StatementNode> statements;
    private final ExprNode returnExpr;

    public MethodDeclNode(String methodType, String methodName, List<Pair<String, String>> methodArgs,
            List<VarDeclNode> varDecls, List<StatementNode> statements, ExprNode returnExpr) {
        this.methodType = methodType;
        this.methodName = methodName;
        this.methodArgs = methodArgs;
        this.varDecls = varDecls;
        this.statements = statements;
        this.returnExpr = returnExpr;
    }

    public String getMethodType() {
        return methodType;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<Pair<String, String>> getMethodArgs() {
        return new ArrayList<>(methodArgs);
    }

    public List<VarDeclNode> getVarDecls() {
        return new ArrayList<>(varDecls);
    }

    public String prettyPrint(String identation) {
        String str = "MethodDeclNode:" + "\n" + identation + "\t" + methodType;
        str += "\n" + identation + "\t" + methodName;
        for (Pair<String, String> pairArg : methodArgs) {
            str += "\n" + identation + "\t" + pairArg.first() + " " + pairArg + pairArg.second();
        }
        for (VarDeclNode varDecl : varDecls) {
            str += "\n" + varDecl.prettyPrint(identation + "\t");
        }
        for (StatementNode statement : statements) {
            str += "\n" + statement.prettyPrint(identation + "\t");
        }
        str += "\n" + returnExpr.prettyPrint(identation + "\t");
        return identation + str;
    }

    public void accept(BuilderVisitor vis, Map<String, MethodType> fields) {
        vis.visit(this, fields);
    }

}
