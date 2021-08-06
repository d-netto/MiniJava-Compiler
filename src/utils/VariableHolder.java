package utils;

import semantics.types.Variable;

public class VariableHolder {

    private String varName;

    private Variable variable;

    public VariableHolder(String varName, Variable variable) {
        this.varName = varName;
        this.variable = variable;
    }

    public String getVarName() {
        return varName;
    }

    public Variable getVariable() {
        return variable;
    }

}
