package semantics.types;

public class Variable {

    public final Type type;
    public boolean hasBeenSet;

    public Variable(Type type) {
        this.type = type;
        this.hasBeenSet = false;
    }

    @Override public boolean equals(Object otherVariable) {
        return otherVariable instanceof Variable && type.equals(((Variable) otherVariable).type)
                && hasBeenSet == ((Variable) otherVariable).hasBeenSet;
    }

}
