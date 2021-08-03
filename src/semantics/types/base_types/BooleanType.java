package semantics.types.base_types;

import semantics.types.Type;

public class BooleanType implements Type {

    @Override public boolean equals(Object otherType) {
        return otherType instanceof BooleanType;
    }

    @Override public int hashCode() {
        return 42;
    }

}
