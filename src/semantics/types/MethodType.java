package semantics.types;

import java.util.List;
import java.util.Map;

import utils.Pair;

public class MethodType extends Type {

    private final Type returnType;
    private final List<Pair<String, Variable>> arguments;
    private final Map<String, Variable> varsDecl;

    public MethodType(Type returnType, List<Pair<String, Variable>> arguments,
            Map<String, Variable> variablesDeclared) {
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

    @Override public boolean isMethodType() {
        return true;
    }

    public Type getReturnType() {
        return returnType;
    }

    public List<Pair<String, Variable>> getArguments() {
        return arguments;
    }

    public Map<String, Variable> getVarsDecl() {
        return varsDecl;
    }

}
