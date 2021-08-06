package semantics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import parser.ast.ClassNode;
import parser.ast.GoalNode;
import parser.ast.MethodDeclNode;
import parser.ast.VarDeclNode;
import semantics.types.ClassType;
import semantics.types.MethodType;
import semantics.types.Type;
import semantics.types.Variable;
import semantics.types.base_types.BooleanType;
import semantics.types.base_types.IntArrayType;
import semantics.types.base_types.IntType;
import utils.ClassHolder;
import utils.Pair;
import utils.VariableHolder;

public class BuilderVisitor {

    private Map<String, ClassHolder> pendingClasses;

    private Map<String, ClassType> classSymbolTable;

    public BuilderVisitor() {
        this.pendingClasses = new HashMap<>();
        this.classSymbolTable = new HashMap<>();
    }

    public Map<String, ClassType> getClassSymbolTable() {
        return new HashMap<>(classSymbolTable);
    }

    public ClassType getClassType(String className, int line) {
        if (classSymbolTable.containsKey(className)) {
            return classSymbolTable.get(className);
        } else if (pendingClasses.containsKey(className)) {
            return pendingClasses.get(className).getClassType();
        } else {
            ClassType pendingClass = new ClassType(Optional.empty(), new HashMap<>(), new HashMap<>());
            pendingClasses.put(className, new ClassHolder(line, pendingClass));
            classSymbolTable.put(className, pendingClass);
            return pendingClass;
        }
    }

    public Type getType(String className, int line) {
        if (className.equals("boolean")) {
            return new BooleanType();
        } else if (className.equals("int")) {
            return new IntType();
        } else if (className.equals("int[]")) {
            return new IntArrayType();
        } else {
            return getClassType(className, line);
        }
    }

    public void addClassType(String className, ClassType classType) {
        if (pendingClasses.containsKey(className)) {
            ClassType pendingClass = pendingClasses.get(className).getClassType();
            pendingClass.setExtendsFrom(classType.getExtendsFrom());
            pendingClass.setFields(classType.getFields());
            pendingClass.setMethods(classType.getMethods());
            pendingClasses.remove(className);
            return;
        } else if (classSymbolTable.containsKey(className)) {
            throw new AssertionError(String.format("Class \"%s\" has been declared twice", className));
        }
        classSymbolTable.put(className, classType);
    }

    public void visit(GoalNode node) {
        for (ClassNode classNode : node.getClasses()) {
            classNode.accept(this);
        }
        if (!pendingClasses.isEmpty()) {
            String className = pendingClasses.keySet().iterator().next();
            throw new AssertionError(String.format("Class \"%s\" used in line %d was not defined", className,
                    pendingClasses.get(className).getLine()));
        }
    }

    public void visit(ClassNode node) {
        List<ClassType> allParents = new ArrayList<>();
        Optional<ClassType> extendsFrom = Optional.empty();
        if (node.getExtendsFrom().isPresent()) {
            allParents = getClassType(node.getExtendsFrom().get(), node.getLine()).getAllParents();
            extendsFrom = Optional.of(getClassType(node.getExtendsFrom().get(), node.getLine()));
        }
        Map<String, Variable> fields = new HashMap<>();
        Map<String, MethodType> methods = new HashMap<>();
        ClassType thisClass = new ClassType(extendsFrom, fields, methods);
        addClassType(node.getClassName(), thisClass);
        for (VarDeclNode varDecl : node.getVarDecls()) {
            varDecl.accept(this, fields);
        }
        for (MethodDeclNode methodDecl : node.getMethodDecls()) {
            methodDecl.accept(this, methods);
            for (ClassType parent : allParents) {
                String methodName = methodDecl.getMethodName();
                Map<String, MethodType> parentMethods = parent.getMethods();
                if (parentMethods.containsKey(methodDecl.getMethodName())) {
                    MethodType methodFromParent = parentMethods.get(methodName);
                    Iterator<VariableHolder> argumentsCurrentMethodIter = methods.get(methodName).getArguments()
                            .iterator();
                    Iterator<VariableHolder> argumentsMethodFromParentIter = methodFromParent.getArguments().iterator();
                    while (argumentsCurrentMethodIter.hasNext()) {
                        assert argumentsCurrentMethodIter.next().getVariable().getType()
                                .equals(argumentsMethodFromParentIter.next().getVariable().getType()) : String.format(
                                        "Overwritten method in %d should be called with the same parameter types as the method in parent class",
                                        node.getLine());
                    }
                    assert !(argumentsMethodFromParentIter.hasNext()) : String
                            .format("Number of arguments mismatch in overwritten method from line %d", node.getLine());
                }
            }
        }
    }

    public void visit(VarDeclNode node, Map<String, Variable> fields) {
        Type varType = getType(node.getVarType(), node.getLine());
        fields.put(node.getVarName(), new Variable(varType));
    }

    public void visit(MethodDeclNode node, Map<String, MethodType> methods) {
        Type returnType = getType(node.getMethodType(), node.getLine());
        Set<String> varNamesSeenSoFar = new HashSet<>();
        List<VariableHolder> arguments = new ArrayList<>();
        for (Pair<String, String> argument : node.getMethodArgs()) {
            varNamesSeenSoFar.add(argument.second());
            Variable variable = new Variable(getType(argument.first(), node.getLine()));
            variable.setVariable();
            arguments.add(new VariableHolder(argument.second(), variable));
        }
        Map<String, Variable> varsDecls = new HashMap<>();
        for (VarDeclNode varDecl : node.getVarDecls()) {
            String varName = varDecl.getVarName();
            if (varNamesSeenSoFar.contains(varName) || varsDecls.containsKey(varName)) {
                throw new AssertionError(String.format("Variable  \"%s\" has already been declared", varName));
            }
            varDecl.accept(this, varsDecls);
        }
        methods.put(node.getMethodName(), new MethodType(returnType, arguments, varsDecls));
    }

}
