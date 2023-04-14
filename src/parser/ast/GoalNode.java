package parser.ast;

import java.util.List;

import codegen_simple.SimpleCodegenVisitor;
import parser.ast.base_abs_classes.StatementNode;
import semantics.BuilderVisitor;
import semantics.TypesVisitor;

public class GoalNode {

    private final int line;
    private final String mainClassName;
    private final String argName;
    private final StatementNode statement;
    private final List<ClassNode> classes;

    public GoalNode(int line, String mainClassName, String argName, StatementNode statement, List<ClassNode> classes) {
        this.line = line;
        this.mainClassName = mainClassName;
        this.argName = argName;
        this.statement = statement;
        this.classes = classes;
    }

    public int getLine() {
        return line;
    }

    public StatementNode getStatement() {
        return statement;
    }

    public List<ClassNode> getClasses() {
        return classes;
    }

    public String prettyString(String identation) {
        StringBuilder strBuilder = new StringBuilder("GoalNode:" + "\n" + identation + "\t" + mainClassName);
        strBuilder.append("\n" + identation + "\t" + argName);
        strBuilder.append("\n" + statement.prettyString(identation + "\t"));
        for (ClassNode _class : classes) {
            strBuilder.append("\n" + _class.prettyString(identation + "\t"));
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
