package org.proto.serdes.type;

import com.google.protobuf.Descriptors;
import org.proto.serdes.code.TypeCode;

import java.util.*;

public class MapFieldClass extends CompoundTypeClass {
    private final TypeClass keyType;

    public MapFieldClass(boolean inner, TypeClass keyType, TypeClass valueType) {
        super(Map.class, inner, valueType);
        this.keyType = keyType;
        keyType.setParent(this);
    }

    @Override
    public TypeCode getGenericProtoCode() {
        return getTypeCode(rawClass, TypeClass::getProtoCode);
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
