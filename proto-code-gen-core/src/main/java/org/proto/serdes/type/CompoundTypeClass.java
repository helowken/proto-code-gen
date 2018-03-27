package org.proto.serdes.type;

import org.proto.serdes.ProtoSerdesFactory;
import org.proto.serdes.code.TypeCode;
import org.proto.serdes.ProtoBasicTypes;
import com.google.protobuf.Descriptors;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class CompoundTypeClass extends AbstractTypeClass {
    final TypeClass valueType;
    private String protoClassName;

    CompoundTypeClass(Class<?> rawClass, boolean inner, TypeClass valueType) {
        super(rawClass, inner);
        this.valueType = valueType;
        valueType.setParent(this);
    }

    @Override
    public Optional<String> getProtoClassName() {
        return Optional.ofNullable(protoClassName);
    }

    @Override
    public TypeCode getInstanceProtoCode() {
        return getTypeCode(getInstanceClass(), TypeClass::getProtoCode);
    }

    public TypeClass getValueType() {
        return valueType;
    }

    @Override
    public Collection<TypeClass> getChildren() {
        return Collections.singleton(valueType);
    }

    @Override
    public void compute(Descriptors.Descriptor descriptor) {
        Descriptors.Descriptor targetDescriptor = descriptor;
        if (inner) {
            protoClassName = descriptor.getName();
            Optional<Descriptors.Descriptor> opt = getChildDescriptor(descriptor, 1, 0);
            if (opt.isPresent())
                targetDescriptor = opt.get();
            else
                return;
        }
        computeValueType(targetDescriptor);
        if (valueType.isProto())
            ProtoSerdesFactory.getInstance().getSerdes(valueType);
    }

    Optional<Descriptors.Descriptor> getChildDescriptor(Descriptors.Descriptor descriptor, int checkSize, int index) {
        List<Descriptors.FieldDescriptor> fieldDescriptors = descriptor.getFields();
        if (fieldDescriptors.size() != checkSize)
            throw new RuntimeException("Invalid field descriptiors size, expected is " + checkSize
                    + ", but now is " + fieldDescriptors.size() + ", field descriptors are: " + fieldDescriptors
                    + ", type class is: " + this);
        Descriptors.FieldDescriptor fieldDescriptor = fieldDescriptors.get(index);
        if (!ProtoBasicTypes.contains(fieldDescriptor.getType()))
            return Optional.of(fieldDescriptor.getMessageType());
        return Optional.empty();
    }

    abstract void computeValueType(Descriptors.Descriptor targetDescriptor);
}
