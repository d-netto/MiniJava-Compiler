package semantics.types;

public class Variable {

    public final Type type;
    public boolean hasBeenSet;

    public Variable(Type type) {
        this.type = type;
        this.hasBeenSet = false;
    }

}
