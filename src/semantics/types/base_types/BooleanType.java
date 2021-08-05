package semantics.types.base_types;

import semantics.types.Type;

public class BooleanType extends Type {

    @Override public boolean equals(Object otherType) {
        return otherType instanceof BooleanType;
    }

    @Override public int hashCode() {
        return 42;
    }

    @Override public boolean isBooleanType() {
        return true;
    }

}
