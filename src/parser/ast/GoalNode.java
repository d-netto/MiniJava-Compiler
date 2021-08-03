package parser.ast;

import java.util.List;

import parser.ast.interfaces.StatementNode;
import semantics.BuilderVisitor;

public class GoalNode {

    public final String mainClassName;
    public final String argName;
    public final StatementNode statement;
    public final List<ClassNode> classes;

    public GoalNode(String mainClassName, String argName, StatementNode statement, List<ClassNode> classes) {
        this.mainClassName = mainClassName;
        this.argName = argName;
        this.statement = statement;
        this.classes = classes;
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

}
