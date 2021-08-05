package parser.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import semantics.BuilderVisitor;

public class ClassNode {

    private final String className;
    private final Optional<String> extendsFrom;
    private final List<VarDeclNode> varDecls;
    private final List<MethodDeclNode> methodDecls;

    public ClassNode(String className, Optional<String> extendsFrom, List<VarDeclNode> varDecls,
            List<MethodDeclNode> methodDecls) {
        this.className = className;
        this.extendsFrom = extendsFrom;
        this.varDecls = varDecls;
        this.methodDecls = methodDecls;
    }

    public String getClassName() {
        return className;
    }

    public Optional<String> getExtendsFrom() {
        return extendsFrom;
    }

    public List<VarDeclNode> getVarDecls() {
        return new ArrayList<>(varDecls);
    }

    public List<MethodDeclNode> getMethodDecls() {
        return new ArrayList<>(methodDecls);
    }

    public String prettyPrint(String identation) {
        String str = "ClassNode:" + "\n" + identation + "\t" + (extendsFrom.isPresent() ? extendsFrom.get() : "");
        for (VarDeclNode varDecl : varDecls) {
            str += "\n" + varDecl.prettyPrint(identation + "\t");
        }
        for (MethodDeclNode methodDecl : methodDecls) {
            str += "\n" + methodDecl.prettyPrint(identation + "\t");
        }
        return identation + str;
    }

    public void accept(BuilderVisitor vis) {
        vis.visit(this);
    }

}
