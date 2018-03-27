package org.proto.serdes.type;

import com.google.protobuf.Descriptors;
import org.proto.serdes.ProtoUtils;
import org.proto.serdes.info.ProtoClassInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class FieldClass extends AbstractTypeClass {

    public FieldClass(Class<?> rawClass) {
        super(rawClass, false);
    }

    @Override
    public Optional<String> getProtoClassName() {
        return ProtoUtils.getProtoClassInfo(rawClass).map(ProtoClassInfo::getProtoClass);
    }

    @Override
    public void compute(Descriptors.Descriptor descriptor) {
    }

    @Override
    public Collection<TypeClass> getChildren() {
        return Collections.emptyList();
    }

    @Override
    Class<?> getDefaultInstanceClass() {
        return rawClass;
    }
}
