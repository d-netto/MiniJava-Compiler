package parser.ast;


public class VarDeclNode {
    
    private int lineDecl;
    private String varType;
    private String varName;
    
    public VarDeclNode(int lineDecl, String varType, String varName) {
        this.lineDecl = lineDecl;
        this.varType = varType;
        this.varName = varName;
    }

}
