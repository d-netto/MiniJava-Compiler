package parser.ast;

import java.util.List;
import java.util.Optional;

public class ClassNode {

    private String className;
    private Optional<String> extendsFrom;
    private List<VarDeclNode> varDecls;
    private List<MethodDeclNode> methodDecls;

    public ClassNode(String className, Optional<String> extendsFrom, List<VarDeclNode> varDecls,
            List<MethodDeclNode> methodDecls) {
        this.className = className;
        this.extendsFrom = extendsFrom;
        this.varDecls = varDecls;
        this.methodDecls = methodDecls;
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

}
