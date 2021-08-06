package parser.ast;

import java.util.Map;

import semantics.BuilderVisitor;
import semantics.TypesVisitor;
import semantics.types.Variable;

public class VarDeclNode {

    private final int line;
    private final String varType;
    private final String varName;

    public VarDeclNode(int line, String varType, String varName) {
        this.line = line;
        this.varType = varType;
        this.varName = varName;
    }

    public int getLine() {
        return line;
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

    public void accept(TypesVisitor vis) {
        vis.visit(this);
    }

}
