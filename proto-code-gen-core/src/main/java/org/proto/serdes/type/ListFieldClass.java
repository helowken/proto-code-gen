package org.proto.serdes.type;

import java.util.ArrayList;

public class ListFieldClass extends CollectionFieldClass {
    public ListFieldClass(Class<?> rawClass, boolean inner, TypeClass valueType) {
        super(rawClass, inner, valueType);
    }

    @Override
    Class<?> getDefaultInstanceClass() {
        return ArrayList.class;
    }
}
