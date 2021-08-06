package utils;

import semantics.types.Type;

public class VariableHolder {

    private String varName;

    private Type type;

    public VariableHolder(String varName, Type type) {
        this.varName = varName;
        this.type = type;
    }

    public String getVarName() {
        return varName;
    }

    public Type getType() {
        return type;
    }

}
