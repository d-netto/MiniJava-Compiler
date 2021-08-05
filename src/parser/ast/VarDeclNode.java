package parser.ast;

import java.util.Map;

import semantics.BuilderVisitor;
import semantics.types.Variable;

public class VarDeclNode {

    private final int lineDecl;
    private final String varType;
    private final String varName;

    public VarDeclNode(int lineDecl, String varType, String varName) {
        this.lineDecl = lineDecl;
        this.varType = varType;
        this.varName = varName;
    }

    public String getVarType() {
        return varType;
    }

    public String getVarName() {
        return varName;
    }

    public String prettyPrint(String identation) {
        return identation + "VarDeclNode: " + "\n" + identation + "\t" + varType + "\n" + identation + "\t" + varName;
    }

    public void accept(BuilderVisitor vis, Map<String, Variable> fields) {
        vis.visit(this, fields);
    }

}
