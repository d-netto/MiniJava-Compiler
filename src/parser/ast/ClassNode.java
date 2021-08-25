package parser.ast;

import java.util.List;
import java.util.Optional;

import codegen_simple.SimpleCodegenVisitor;
import semantics.BuilderVisitor;
import semantics.TypesVisitor;

public class ClassNode {

    private final int line;
    private final String className;
    private final Optional<String> extendsFrom;
    private final List<VarDeclNode> varDecls;
    private final List<MethodDeclNode> methodDecls;

    public ClassNode(int line, String className, Optional<String> extendsFrom, List<VarDeclNode> varDecls,
            List<MethodDeclNode> methodDecls) {
        this.line = line;
        this.className = className;
        this.extendsFrom = extendsFrom;
        this.varDecls = varDecls;
        this.methodDecls = methodDecls;
    }

    public int getLine() {
        return line;
    }

    public String getClassName() {
        return className;
    }

    public Optional<String> getExtendsFrom() {
        return extendsFrom;
    }

    public List<VarDeclNode> getVarDecls() {
        return varDecls;
    }

    public List<MethodDeclNode> getMethodDecls() {
        return methodDecls;
    }

    public String prettyString(String identation) {
        StringBuilder strBuilder = new StringBuilder(
                "ClassNode:" + "\n" + identation + "\t" + (extendsFrom.isPresent() ? extendsFrom.get() : ""));
        for (VarDeclNode varDecl : varDecls) {
            strBuilder.append("\n" + varDecl.prettyString(identation + "\t"));
        }
        for (MethodDeclNode methodDecl : methodDecls) {
            strBuilder.append("\n" + methodDecl.prettyString(identation + "\t"));
        }
        return identation + strBuilder.toString();
    }

    public void accept(BuilderVisitor vis) {
        vis.visit(this);
    }

    public void accept(TypesVisitor vis) {
        vis.visit(this);
    }

    public void accept(SimpleCodegenVisitor vis) {
        vis.visit(this);
    }

}
