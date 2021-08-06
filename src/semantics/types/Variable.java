package semantics.types;

public class Variable extends Type {

    private final Type type;
    private boolean hasBeenSet;

    public Variable(Type type) {
        this.type = type;
        this.hasBeenSet = false;
    }

    @Override public int hashCode() {
        return type.hashCode() + (hasBeenSet ? 0 : 1);
    }

    public Type getType() {
        return type;
    }

    @Override public boolean hasBeenSet() {
        return hasBeenSet;
    }

    public void setVariable() {
        hasBeenSet = true;
    }

    @Override public boolean isVariableType() {
        return true;
    }

}
