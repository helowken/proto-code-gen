package org.proto.serdes.info;


import org.proto.serdes.type.TypeClass;

public class ProtoFieldInfo implements FieldInfo {
    private final int index;
    private final String protoField;
    private final TypeClass typeClass;

    public ProtoFieldInfo(int index, String protoField, TypeClass typeClass) {
        if (index < 1)
            throw new IllegalArgumentException("Index can not be < 1");
        this.index = index;
        this.protoField = protoField;
        this.typeClass = typeClass;
    }

    public int getIndex() {
        return index;
    }

    public String getProtoField() {
        return protoField;
    }

    public TypeClass getTypeClass() {
        return typeClass;
    }

}
