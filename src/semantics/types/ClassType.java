package semantics.types;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ClassType extends Type {

    private final Optional<ClassType> extendsFrom;
    private final Map<String, Variable> fields;
    private final Map<String, MethodType> methods;

    public ClassType(Optional<ClassType> extendsFrom, Map<String, Variable> fields, Map<String, MethodType> methods) {
        this.extendsFrom = extendsFrom;
        this.fields = fields;
        this.methods = methods;
    }

    @Override public boolean equals(Object otherType) {
        return otherType instanceof ClassType && extendsFrom.equals(((ClassType) otherType).extendsFrom)
                && fields.equals(((ClassType) otherType).fields) && methods.equals(((ClassType) otherType).methods);
    }

    @Override public int hashCode() {
        return extendsFrom.hashCode() + fields.hashCode() + methods.hashCode();
    }

    public boolean containsClassAsParent(ClassType otherClassType) {
        if (this.equals(otherClassType)) {
            return false;
        } else if (extendsFrom.isEmpty()) {
            return true;
        } else {
            return extendsFrom.get().containsClassAsParent(otherClassType);
        }
    }

    @Override public boolean isClassType() {
        return true;
    }

    public Map<String, Variable> getFields() {
        return new HashMap<>(fields);
    }

    public Map<String, MethodType> getMethods() {
        return new HashMap<>(methods);
    }

    public boolean inMethods(String methodName) {
        return fields.containsKey(methodName);
    }

}
