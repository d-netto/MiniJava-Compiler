package semantics.types;

import java.util.List;
import java.util.Map;

import utils.VariableHolder;

public class MethodType extends Type {

    private final Type returnType;
    private final List<VariableHolder> arguments;
    private final Map<String, Variable> varsDecl;

    public MethodType(Type returnType, List<VariableHolder> arguments, Map<String, Variable> variablesDeclared) {
        this.returnType = returnType;
        this.arguments = arguments;
        this.varsDecl = variablesDeclared;
    }

    @Override public boolean equals(Object otherType) {
        return otherType instanceof MethodType && returnType.equals(((MethodType) otherType).returnType)
                && arguments.equals(((MethodType) otherType).arguments)
                && varsDecl.equals(((MethodType) otherType).varsDecl);
    }

    @Override public int hashCode() {
        return returnType.hashCode() + arguments.hashCode() + varsDecl.hashCode();
    }

    public Type getReturnType() {
        return returnType;
    }

    public List<VariableHolder> getArguments() {
        return arguments;
    }

    public Map<String, Variable> getVarsDecl() {
        return varsDecl;
    }

    @Override public boolean isMethodType() {
        return true;
    }

}
