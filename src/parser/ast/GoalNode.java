package parser.ast;

import parser.ast.statement.StatementNode;

public class GoalNode {
    
    private String mainClassName;
    private String argName;
    private StatementNode statement;
    
    public GoalNode(String mainClassName, String argName, StatementNode statement) {
        this.mainClassName = mainClassName;
        this.argName = argName;
        this.statement = statement;
    }

}
