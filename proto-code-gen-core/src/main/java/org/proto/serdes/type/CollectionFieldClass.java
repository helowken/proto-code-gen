package org.proto.serdes.type;

import com.google.protobuf.Descriptors;
import org.proto.serdes.code.TypeCode;

import java.util.Collection;

public abstract class CollectionFieldClass extends CompoundTypeClass {

    CollectionFieldClass(Class<?> rawClass, boolean inner, TypeClass valueType) {
        super(rawClass, inner, valueType);
    }

    @Override
    public void computeValueType(Descriptors.Descriptor targetDescriptor) {
        valueType.compute(targetDescriptor);
    }

    @Override
    public TypeCode getGenericProtoCode() {
        return getTypeCode(Collection.class, TypeClass::getProtoCode);
    }
}
