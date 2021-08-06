package semantics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import utils.Pair;

public class BuilderVisitor {

    private Map<String, ClassType> classSymbolTable;

    public BuilderVisitor() {
        this.classSymbolTable = new HashMap<>();
    }

    public Map<String, ClassType> getClassSymbolTable() {
        return new HashMap<>(classSymbolTable);
    }

    public ClassType getClassType(String className, int line) {
        if (classSymbolTable.containsKey(className)) {
            return classSymbolTable.get(className);
        }
        throw new AssertionError(String.format("Semantic error: class %s was not declared in line", className, line));
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
        if (classSymbolTable.containsKey(className)) {
            throw new AssertionError(String.format("Class \"%s\" has been declared twice", className));
        }
        classSymbolTable.put(className, classType);
    }

    public void visit(GoalNode node) {
        for (ClassNode classNode : node.getClasses()) {
            classNode.accept(this);
        }
    }

    public void visit(ClassNode node) {
        Optional<ClassType> extendsFrom = Optional.empty();
        if (node.getExtendsFrom().isPresent()) {
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
        }
    }

    public void visit(VarDeclNode node, Map<String, Variable> fields) {
        Type varType = getType(node.getVarType(), node.getLine());
        fields.put(node.getVarName(), new Variable(varType));
    }

    public void visit(MethodDeclNode node, Map<String, MethodType> methods) {
        Type returnType = getType(node.getMethodType(), node.getLine());
        Set<String> varNames = new HashSet<>();
        List<Pair<String, Variable>> arguments = new ArrayList<>();
        for (Pair<String, String> argument : node.getMethodArgs()) {
            varNames.add(argument.second());
            arguments.add(new Pair<String, Variable>(argument.second(),
                    new Variable(getType(argument.first(), node.getLine()))));
        }
        Map<String, Variable> varsDecls = new HashMap<>();
        for (VarDeclNode varDecl : node.getVarDecls()) {
            if (varNames.contains(varDecl.getVarName()) || varsDecls.containsKey(varDecl.getVarName())) {
                throw new AssertionError(
                        String.format("Variable  \"%s\" has already been declared", varDecl.getVarName()));
            }
            varDecl.accept(this, varsDecls);
        }
        methods.put(node.getMethodName(), new MethodType(returnType, arguments, varsDecls));
    }

}
