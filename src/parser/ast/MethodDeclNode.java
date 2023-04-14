package parser.ast;

import java.util.List;

import codegen_simple.SimpleCodegenVisitor;
import parser.ast.base_abs_classes.ExprNode;
import parser.ast.base_abs_classes.StatementNode;
import semantics.BuilderVisitor;
import semantics.TypesVisitor;
import semantics.types.MethodType;
import utils.Pair;

public class MethodDeclNode {

    private final int line;
    private final String methodType;
    private final String methodName;
    private final List<Pair<String, String>> methodArgs;
    private final List<VarDeclNode> varDecls;
    private final List<StatementNode> statements;
    private final ExprNode returnExpr;

    public MethodDeclNode(int line, String methodType, String methodName, List<Pair<String, String>> methodArgs,
            List<VarDeclNode> varDecls, List<StatementNode> statements, ExprNode returnExpr) {
        this.line = line;
        this.methodType = methodType;
        this.methodName = methodName;
        this.methodArgs = methodArgs;
        this.varDecls = varDecls;
        this.statements = statements;
        this.returnExpr = returnExpr;
    }

    public int getLine() {
        return line;
    }

    public String getMethodType() {
        return methodType;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<Pair<String, String>> getMethodArgs() {
        return methodArgs;
    }

    public List<VarDeclNode> getVarDecls() {
        return varDecls;
    }

    public List<StatementNode> getStatements() {
        return statements;
    }

    public ExprNode getReturnExpr() {
        return returnExpr;
    }

    public String prettyString(String identation) {
        StringBuilder strBuilder = new StringBuilder("MethodDeclNode:" + "\n" + identation + "\t" + methodType);
        strBuilder.append("\n" + identation + "\t" + methodName);
        for (Pair<String, String> pairArg : methodArgs) {
            strBuilder.append("\n" + identation + "\t" + pairArg.first() + " " + pairArg + pairArg.second());
        }
        for (VarDeclNode varDecl : varDecls) {
            strBuilder.append("\n" + varDecl.prettyString(identation + "\t"));
        }
        for (StatementNode statement : statements) {
            strBuilder.append("\n" + statement.prettyString(identation + "\t"));
        }
        strBuilder.append("\n" + returnExpr.prettyString(identation + "\t"));
        return identation + strBuilder.toString();
    }

    public Pair<String, MethodType> accept(BuilderVisitor vis) {
        return vis.visit(this);
    }

    public void accept(TypesVisitor vis) {
        vis.visit(this);
    }

    public void accept(SimpleCodegenVisitor vis) {
        vis.visit(this);
    }

}
