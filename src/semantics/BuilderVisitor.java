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
import semantics.types.base_types.BooleanType;
import semantics.types.base_types.IntArrayType;
import semantics.types.base_types.IntType;
import utils.Pair;

public class BuilderVisitor {

  private Map<String, Pair<Integer, ClassType>> pendingClasses;

  private Map<String, ClassType> classSymbolTable;

  public BuilderVisitor() {
    this.pendingClasses = new HashMap<>();
    this.classSymbolTable = new HashMap<>();
  }

  public Map<String, ClassType> getClassSymbolTable() {
    return classSymbolTable;
  }

  public ClassType getClassType(String className, int line) {
    if (classSymbolTable.containsKey(className)) {
      return classSymbolTable.get(className);
    } else if (pendingClasses.containsKey(className)) {
      return pendingClasses.get(className).second();
    } else {
      ClassType pendingClass =
          new ClassType(className, Optional.empty(), new ArrayList<>(), new ArrayList<>());
      pendingClasses.put(className, new Pair<Integer, ClassType>(line, pendingClass));
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
      ClassType pendingClass = pendingClasses.get(className).second();
      pendingClass.copy(classType);
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
      throw new AssertionError(
          String.format(
              "Class \"%s\" used in line %d was not defined",
              className, pendingClasses.get(className).first()));
    }
  }

  public void visit(ClassNode node) {
    List<ClassType> allParents = new ArrayList<>();
    Optional<ClassType> extendsFrom = Optional.empty();
    if (node.getExtendsFrom().isPresent()) {
      allParents = getClassType(node.getExtendsFrom().get(), node.getLine()).getAllParents();
      extendsFrom = Optional.of(getClassType(node.getExtendsFrom().get(), node.getLine()));
    }
    List<Pair<String, Type>> fieldsSorted = new ArrayList<>();
    List<Pair<String, MethodType>> methodsSorted = new ArrayList<>();
    ClassType thisClass =
        new ClassType(node.getClassName(), extendsFrom, fieldsSorted, methodsSorted);
    addClassType(node.getClassName(), thisClass);
    for (VarDeclNode varDecl : node.getVarDecls()) {
      fieldsSorted.add(varDecl.accept(this));
    }
    for (MethodDeclNode methodDecl : node.getMethodDecls()) {
      Pair<String, MethodType> methodPair = methodDecl.accept(this);
      MethodType currentMethod = methodPair.second();
      methodsSorted.add(methodPair);
      for (ClassType parent : allParents) {
        String methodName = methodDecl.getMethodName();
        Map<String, MethodType> parentMethods = parent.getMethods();
        if (parentMethods.containsKey(methodDecl.getMethodName())) {
          MethodType methodFromParent = parentMethods.get(methodName);
          assert methodFromParent.getReturnType().equals(currentMethod.getReturnType())
              : String.format(
                  "Overwritten method in %d should have the same return type as the method in parent class",
                  node.getLine());
          Iterator<Pair<String, Type>> argumentsCurrentMethodIter =
              currentMethod.getArgumentsSorted().iterator();
          Iterator<Pair<String, Type>> argumentsMethodFromParentIter =
              methodFromParent.getArgumentsSorted().iterator();
          while (argumentsCurrentMethodIter.hasNext()) {
            assert argumentsCurrentMethodIter
                    .next()
                    .second()
                    .equals(argumentsMethodFromParentIter.next().second())
                : String.format(
                    "Overwritten method in %d should be called with the same parameter types as the method in parent class",
                    node.getLine());
          }
          assert !(argumentsMethodFromParentIter.hasNext())
              : String.format(
                  "Number of arguments mismatch in overwritten method from line %d",
                  node.getLine());
        }
      }
    }
    thisClass.update();
  }

  public Pair<String, Type> visit(VarDeclNode node) {
    return new Pair<>(node.getVarName(), getType(node.getVarType(), node.getLine()));
  }

  public Pair<String, MethodType> visit(MethodDeclNode node) {
    Type returnType = getType(node.getMethodType(), node.getLine());
    Set<String> varNamesSeenSoFar = new HashSet<>();
    List<Pair<String, Type>> argumentsSorted = new ArrayList<>();
    for (Pair<String, String> argument : node.getMethodArgs()) {
      varNamesSeenSoFar.add(argument.second());
      argumentsSorted.add(
          new Pair<String, Type>(argument.second(), getType(argument.first(), node.getLine())));
    }
    List<Pair<String, Type>> varsDeclsSorted = new ArrayList<>();
    for (VarDeclNode varDecl : node.getVarDecls()) {
      String varName = varDecl.getVarName();
      if (varNamesSeenSoFar.contains(varName)) {
        throw new AssertionError(String.format("Type  \"%s\" has already been declared", varName));
      }
      varNamesSeenSoFar.add(varName);
      varsDeclsSorted.add(varDecl.accept(this));
    }
    return new Pair<String, MethodType>(
        node.getMethodName(), new MethodType(returnType, argumentsSorted, varsDeclsSorted));
  }
}
