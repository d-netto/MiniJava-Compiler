package parser.ast;

import java.util.Map;

import semantics.BuilderVisitor;
import semantics.types.Variable;

public class VarDeclNode {

    public final int lineDecl;
    public final String varType;
    public final String varName;

    public VarDeclNode(int lineDecl, String varType, String varName) {
        this.lineDecl = lineDecl;
        this.varType = varType;
        this.varName = varName;
    }

    public String prettyPrint(String identation) {
        return identation + "VarDeclNode: " + "\n" + identation + "\t" + varType + "\n" + identation + "\t" + varName;
    }

    public void accept(BuilderVisitor vis, Map<String, Variable> fields) {
        vis.visit(this, fields);
    }

}
