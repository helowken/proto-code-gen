package org.proto.serdes.info;

import org.proto.serdes.type.TypeClass;

public interface FieldInfo {
    int getIndex();

    String getProtoField();

    TypeClass getTypeClass();
}
