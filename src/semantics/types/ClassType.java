package semantics.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import utils.Pair;

public class ClassType extends Type {

    private String className;
    private Optional<ClassType> extendsFrom;
    private List<Pair<String, Type>> fieldsSorted;
    private Map<String, Type> fields;
    private List<Pair<String, MethodType>> methodsSorted;
    private Map<String, MethodType> methods;

    public ClassType(String className, Optional<ClassType> extendsFrom, List<Pair<String, Type>> fieldsSorted,
            List<Pair<String, MethodType>> methodsSorted) {
        this.className = className;
        this.extendsFrom = extendsFrom;
        this.fieldsSorted = fieldsSorted;
        fields = new HashMap<>();
        for (Pair<String, Type> fieldPair : fieldsSorted) {
            fields.put(fieldPair.first(), fieldPair.second());
        }
        this.methodsSorted = methodsSorted;
        methods = new HashMap<>();
        for (Pair<String, MethodType> methodPair : methodsSorted) {
            methods.put(methodPair.first(), methodPair.second());
        }
    }

    public String getClassName() {
        return className;
    }

    public Optional<ClassType> getExtendsFrom() {
        return extendsFrom;
    }

    public List<Pair<String, Type>> getFieldsSorted() {
        return fieldsSorted;
    }

    public Map<String, Type> getFields() {
        return fields;
    }

    public List<Pair<String, MethodType>> getMethodsSorted() {
        return methodsSorted;
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

    public void copy(ClassType otherClassType) {
        extendsFrom = otherClassType.extendsFrom;
        fieldsSorted = otherClassType.fieldsSorted;
        fields = otherClassType.fields;
        methodsSorted = otherClassType.methodsSorted;
        methods = otherClassType.methods;
    }

    public void update() {
        for (Pair<String, Type> fieldPair : fieldsSorted) {
            fields.put(fieldPair.first(), fieldPair.second());
        }
        for (Pair<String, MethodType> methodPair : methodsSorted) {
            methods.put(methodPair.first(), methodPair.second());
        }
    }

    @Override public boolean isClassType() {
        return true;
    }

}
