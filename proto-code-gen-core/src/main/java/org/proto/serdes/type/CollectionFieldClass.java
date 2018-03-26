package org.proto.serdes.type;

import com.google.protobuf.Descriptors;

public abstract class CollectionFieldClass extends CompoundTypeClass {

    CollectionFieldClass(Class<?> rawClass, boolean inner, TypeClass valueType) {
        super(rawClass, inner, valueType);
    }

    @Override
    public void computeValueType(Descriptors.Descriptor targetDescriptor) {
        valueType.compute(targetDescriptor);
    }

}
