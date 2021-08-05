package semantics.types.base_types;

import semantics.types.Type;

public class IntType extends Type {

    @Override public boolean equals(Object otherType) {
        return otherType instanceof IntType;
    }

    @Override public int hashCode() {
        return 44;
    }

    @Override public boolean isIntType() {
        return true;
    }

}
