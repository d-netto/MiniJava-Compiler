package semantics.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.Pair;

public class MethodType extends Type {

    private final Type returnType;
    private List<Pair<String, Type>> argumentsSorted;
    private final Map<String, Type> arguments;
    private List<Pair<String, Type>> varsDeclSorted;
    private final Map<String, Type> varsDecl;

    public MethodType(Type returnType, List<Pair<String, Type>> argumentsSorted,
            List<Pair<String, Type>> varsDeclSorted) {
        this.returnType = returnType;
        this.argumentsSorted = argumentsSorted;
        arguments = new HashMap<>();
        for (Pair<String, Type> fieldPair : argumentsSorted) {
            arguments.put(fieldPair.first(), fieldPair.second());
        }
        this.varsDeclSorted = varsDeclSorted;
        varsDecl = new HashMap<>();
        for (Pair<String, Type> methodPair : varsDeclSorted) {
            varsDecl.put(methodPair.first(), methodPair.second());
        }
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

    public List<Pair<String, Type>> getArgumentsSorted() {
        return argumentsSorted;
    }

    public Map<String, Type> getArguments() {
        return arguments;
    }

    public List<Pair<String, Type>> getVarsDeclSorted() {
        return varsDeclSorted;
    }

    public Map<String, Type> getVarsDecl() {
        return varsDecl;
    }

    @Override public boolean isMethodType() {
        return true;
    }

}
