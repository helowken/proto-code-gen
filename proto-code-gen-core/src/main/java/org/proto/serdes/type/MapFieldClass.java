package org.proto.serdes.type;

import com.google.protobuf.Descriptors;

import java.util.*;

public class MapFieldClass extends CompoundTypeClass {
    private final TypeClass keyType;

    public MapFieldClass(boolean inner, TypeClass keyType, TypeClass valueType) {
        super(Map.class, inner, valueType);
        this.keyType = keyType;
        keyType.setParent(this);
    }

    public TypeClass getKeyType() {
        return keyType;
    }

    @Override
    public Collection<TypeClass> getChildren() {
        List<TypeClass> rs = new ArrayList<>(super.getChildren());
        rs.add(0, keyType);
        return rs;
    }

    @Override
    public void computeValueType(Descriptors.Descriptor targetDescriptor) {
        getChildDescriptor(targetDescriptor, 2, 1).ifPresent(valueType::compute);
    }

    @Override
    Class<?> getDefaultInstanceClass() {
        return HashMap.class;
    }
}
