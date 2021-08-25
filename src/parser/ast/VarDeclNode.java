package parser.ast;

import codegen_simple.SimpleCodegenVisitor;
import semantics.BuilderVisitor;
import semantics.TypesVisitor;
import semantics.types.Type;
import utils.Pair;

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

    public String prettyString(String identation) {
        return identation + "VarDeclNode: " + "\n" + identation + "\t" + varType + "\n" + identation + "\t" + varName;
    }

    public Pair<String, Type> accept(BuilderVisitor vis) {
        return vis.visit(this);
    }

    public void accept(TypesVisitor vis) {
        vis.visit(this);
    }

    public void accept(SimpleCodegenVisitor vis) {
        vis.visit(this);
    }

}
