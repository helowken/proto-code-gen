package org.proto.serdes;

import java.lang.reflect.Field;

public class FieldWrapper {
    Field field;
    final int index;
    final String protoFieldName;

    public FieldWrapper(Field field, int index, String protoFieldName) {
        this.field = field;
        this.index = index;
        this.protoFieldName = protoFieldName;
    }
}
