package semantics.types.base_types;

import semantics.types.Type;

public class IntType implements Type {

    @Override public boolean equals(Object otherType) {
        return otherType instanceof IntType;
    }

    @Override public int hashCode() {
        return 44;
    }

}
