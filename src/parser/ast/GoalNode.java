package parser.ast;

import java.util.ArrayList;
import java.util.List;

import parser.ast.interfaces.StatementNode;
import semantics.BuilderVisitor;
import semantics.TypesVisitor;

public class GoalNode {

    private final String mainClassName;
    private final String argName;
    private final StatementNode statement;
    private final List<ClassNode> classes;

    public GoalNode(String mainClassName, String argName, StatementNode statement, List<ClassNode> classes) {
        this.mainClassName = mainClassName;
        this.argName = argName;
        this.statement = statement;
        this.classes = classes;
    }

    public StatementNode getStatement() {
        return statement;
    }

    public List<ClassNode> getClasses() {
        return new ArrayList<>(classes);
    }

    public String prettyPrint(String identation) {
        String str = "GoalNode:" + "\n" + identation + "\t" + mainClassName;
        str += "\n" + identation + "\t" + argName;
        str += "\n" + statement.prettyPrint(identation + "\t");
        for (ClassNode _class : classes) {
            str += "\n" + _class.prettyPrint(identation + "\t");
        }
        return identation + str;
    }

    public void accept(BuilderVisitor vis) {
        vis.visit(this);
    }

    public void accept(TypesVisitor vis) {
        vis.visit(this);
    }

}
