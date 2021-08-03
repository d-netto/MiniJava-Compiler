package semantics;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import parser.MJParser.Pair;
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

public class BuilderVisitor {

    private Map<String, ClassType> classSymbolTable;

    public BuilderVisitor() {
        this.classSymbolTable = new HashMap<>();
    }

    public ClassType getClassType(String className) {
        if (classSymbolTable.containsKey(className)) {
            return classSymbolTable.get(className);
        }
        throw new RuntimeException("Semantic error: Class " + className + " was not declared");
    }

    public Type getType(String className) {
        if (className.equals("boolean")) {
            return new BooleanType();
        } else if (className.equals("int")) {
            return new IntType();
        } else if (className.equals("int[]")) {
            return new IntArrayType();
        } else {
            return getClassType(className);
        }
    }

    public void addClassType(String className, ClassType classType) {
        if (classSymbolTable.containsKey(className)) {
            throw new RuntimeException("Class \"%s\" has been declared twice");
        }
        classSymbolTable.put(className, classType);
    }

    public void visit(GoalNode node) {
        for (ClassNode classNode : node.classes) {
            classNode.accept(this);
        }
    }

    public void visit(ClassNode node) {
        Optional<ClassType> extendsFrom = Optional.empty();
        if (node.extendsFrom.isPresent()) {
            extendsFrom = Optional.of(getClassType(node.extendsFrom.get()));
        }
        Map<String, Variable> fields = new HashMap<>();
        Map<String, MethodType> methods = new HashMap<>();
        ClassType thisClass = new ClassType(extendsFrom, fields, methods);
        addClassType(node.className, thisClass);
        for (VarDeclNode varDecl : node.varDecls) {
            varDecl.accept(this, fields);
        }
        for (MethodDeclNode methodDecl : node.methodDecls) {
            methodDecl.accept(this, methods);
        }
    }

    public void visit(VarDeclNode node, Map<String, Variable> fields) {
        Type varType = getType(node.varType);
        fields.put(node.varName, new Variable(varType));
    }

    public void visit(MethodDeclNode node, Map<String, MethodType> methods) {
        Type returnType = getType(node.methodType);
        Map<String, Variable> arguments = new HashMap<>();
        for (Pair<String, String> argument : node.methodArgs) {
            arguments.put(argument.second(), new Variable(getType(argument.first())));
        }
        Map<String, Variable> variablesDeclared = new HashMap<>();
        for (VarDeclNode varDecl : node.varDecls) {
            if (arguments.containsKey(varDecl.varName) || variablesDeclared.containsKey(varDecl.varName)) {
                throw new RuntimeException("Variable " + varDecl.varName + " has already been declared");
            }
            varDecl.accept(this, variablesDeclared);
        }
        methods.put(node.methodName, new MethodType(returnType, arguments, variablesDeclared));
    }

}
