package semantics.types.base_types;

import semantics.types.Type;

public class IntArrayType extends Type {

    @Override public boolean equals(Object otherType) {
        return otherType instanceof IntArrayType;
    }

    @Override public int hashCode() {
        return 43;
    }

    @Override public boolean isIntArrayType() {
        return true;
    }

}
