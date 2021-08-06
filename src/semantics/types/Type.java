package semantics.types;

public abstract class Type {

    public boolean hasBeenSet() {
        return true;
    }

    public boolean isBooleanType() {
        return false;
    }

    public boolean isIntArrayType() {
        return false;
    }

    public boolean isIntType() {
        return false;
    }

    public boolean isClassType() {
        return false;
    }

    public boolean isMethodType() {
        return false;
    }

    public boolean isVariableType() {
        return false;
    }

}
