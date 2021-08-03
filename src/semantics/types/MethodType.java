package semantics.types;

import java.util.Map;

public class MethodType {

    public final Type returnType;
    public final Map<String, Variable> arguments;
    public final Map<String, Variable> variablesDeclared;

    public MethodType(Type returnType, Map<String, Variable> arguments, Map<String, Variable> variablesDeclared) {
        this.returnType = returnType;
        this.arguments = arguments;
        this.variablesDeclared = variablesDeclared;
    }

    @Override public boolean equals(Object otherType) {
        return otherType instanceof MethodType && returnType.equals(((MethodType) otherType).returnType)
                && arguments.equals(((MethodType) otherType).arguments)
                && variablesDeclared.equals(((MethodType) otherType).variablesDeclared);
    }

    @Override public int hashCode() {
        return returnType.hashCode() + arguments.hashCode() + variablesDeclared.hashCode();
    }

}
