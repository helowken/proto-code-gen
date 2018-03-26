package org.proto.serdes.type;

import java.util.HashSet;

public class SetFieldClass extends CollectionFieldClass {
    public SetFieldClass(Class<?> rawClass, boolean inner, TypeClass valueType) {
        super(rawClass, inner, valueType);
    }

    @Override
    Class<?> getDefaultInstanceClass() {
        return HashSet.class;
    }
}
