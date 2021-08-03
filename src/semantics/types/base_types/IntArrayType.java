package semantics.types.base_types;

import semantics.types.Type;

public class IntArrayType implements Type {

    @Override public boolean equals(Object otherType) {
        return otherType instanceof IntArrayType;
    }

    @Override public int hashCode() {
        return 43;
    }

}
