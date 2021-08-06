package utils;

import semantics.types.ClassType;

public class ClassHolder {

    private int line;

    private ClassType classType;

    public ClassHolder(int line, ClassType classType) {
        this.line = line;
        this.classType = classType;
    }

    public int getLine() {
        return line;
    }

    public ClassType getClassType() {
        return classType;
    }

}
