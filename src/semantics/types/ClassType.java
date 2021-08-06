package semantics.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ClassType extends Type {

    private Optional<ClassType> extendsFrom;
    private Map<String, Type> fields;
    private Map<String, MethodType> methods;

    public ClassType(Optional<ClassType> extendsFrom, Map<String, Type> fields, Map<String, MethodType> methods) {
        this.extendsFrom = extendsFrom;
        this.fields = fields;
        this.methods = methods;
    }

    public Optional<ClassType> getExtendsFrom() {
        return extendsFrom;
    }

    public Map<String, Type> getFields() {
        return fields;
    }

    public Map<String, MethodType> getMethods() {
        return methods;
    }

    public List<ClassType> getAllParents() {
        List<ClassType> allParents = new ArrayList<>(List.of(this));
        ClassType currentClass = this;
        while (currentClass.extendsFrom.isPresent()) {
            ClassType parent = currentClass.extendsFrom.get();
            allParents.add(parent);
            currentClass = parent;
        }
        return allParents;
    }

    public boolean containsClassAsParent(ClassType otherClassType) {
        return getAllParents().contains(otherClassType);
    }

    public void setExtendsFrom(Optional<ClassType> otherExtendsFrom) {
        extendsFrom = otherExtendsFrom;
    }

    public void setFields(Map<String, Type> otherFields) {
        fields = otherFields;
    }

    public void setMethods(Map<String, MethodType> otherMethods) {
        methods = otherMethods;
    }

    @Override public boolean isClassType() {
        return true;
    }

}
