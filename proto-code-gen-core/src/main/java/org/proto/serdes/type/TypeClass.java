package org.proto.serdes.type;

import org.proto.serdes.code.TypeCode;
import com.google.protobuf.Descriptors;

import java.util.Collection;
import java.util.Optional;

public interface TypeClass {
    Class<?> getRawClass();

    TypeClass getParent();

    void setParent(TypeClass parent);

    Collection<TypeClass> getChildren();

    void compute(Descriptors.Descriptor descriptor);

    Optional<String> getProtoClassName();

    String getProtoClassNameOrError();

    boolean isProto();

    boolean isInner();

    TypeCode getGenericJavaCode();

    TypeCode getInstanceJavaCode();

    TypeCode getProtoCode();

    TypeCode getGenericProtoCode();

    TypeCode getInstanceProtoCode();

    String getGenericJavaString();

    Class<?> getInstanceClass();

    String getRootGenericJavaString();

    <T> T newInstance();
}
