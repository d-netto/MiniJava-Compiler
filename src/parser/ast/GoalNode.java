package parser.ast;

import java.util.List;

import parser.ast.statement.StatementNode;

public class GoalNode {
    
    private String mainClassName;
    private String argName;
    private StatementNode statement;
    private List<ClassNode> classes;
    
    public GoalNode(String mainClassName, String argName, StatementNode statement, List<ClassNode> classes) {
        this.mainClassName = mainClassName;
        this.argName = argName;
        this.statement = statement;
        this.classes = classes;
    }

}
