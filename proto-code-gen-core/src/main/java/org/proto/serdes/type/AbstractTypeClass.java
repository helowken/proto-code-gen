package org.proto.serdes.type;

import org.proto.serdes.ProtoBasicTypes;
import org.proto.serdes.ProtoConst;
import org.proto.serdes.ProtoUtils;
import org.proto.serdes.code.TypeCode;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractTypeClass implements TypeClass {
    final boolean inner;
    final Class<?> rawClass;
    private TypeClass parent;

    AbstractTypeClass(Class<?> rawClass, boolean inner) {
        this.rawClass = rawClass;
        this.inner = inner;
    }

    @Override
    public TypeClass getParent() {
        return parent;
    }

    public void setParent(TypeClass parent) {
        this.parent = parent;
    }

    @Override
    public Class<?> getRawClass() {
        return rawClass;
    }

    @Override
    public String getProtoClassNameOrError() {
        return getProtoClassName().orElseThrow(() -> new RuntimeException("No proto class name found in: " + this));
    }

    @Override
    public boolean isInner() {
        return inner;
    }

    public boolean isProto() {
        return inner || getProtoClassName().isPresent();
    }

    TypeCode getTypeCode(Class<?> clazz, Function<TypeClass, TypeCode> childMapFunc) {
        Collection<TypeClass> cs = getChildren();
        Object[] csCodes = cs.isEmpty() ? null : cs.stream().map(childMapFunc).toArray(Object[]::new);
        Class<?> curr = clazz;
        if (parent != null) {
            curr = ProtoBasicTypes.getBoxClass(clazz).orElse(clazz);
        }
        return new TypeCode(curr, csCodes);
    }

    @Override
    public TypeCode getGenericJavaCode() {
        return getTypeCode(rawClass, TypeClass::getGenericJavaCode);
    }

    @Override
    public TypeCode getInstanceJavaCode() {
        return getTypeCode(getInstanceClass(), TypeClass::getInstanceJavaCode);
    }

    @Override
    public TypeCode getProtoCode() {
        if (isProto()) {
            try {
                String className = ProtoConst.DEFAULT_JAVA_PACKAGE + "." + getProtoClassNameOrError();
                return new TypeCode(ProtoUtils.loadClass(className));
            } catch (Exception e) {
                throw ProtoUtils.wrapError(e);
            }
        }
        Class<?> clazz = ProtoBasicTypes.getProtoPrimitive(rawClass).orElse(rawClass);
        return getTypeCode(clazz, TypeClass::getProtoCode);
    }

    @Override
    public TypeCode getGenericProtoCode() {
        return getProtoCode();
    }

    @Override
    public TypeCode getInstanceProtoCode() {
        return getProtoCode();
    }

    @Override
    public String getGenericJavaString() {
        return getGenericJavaCode().toString();
    }

    @Override
    public String getRootGenericJavaString() {
        return getRoot(this).getGenericJavaString();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[inner: " + inner
                + ", java type: " + getGenericJavaString()
                + ", proto class: " + getProtoClassName().orElse(null)
                + ", root java type: " + getRootGenericJavaString()
                + "]";
    }

    private static TypeClass getRoot(TypeClass typeClass) {
        return Optional.ofNullable(typeClass.getParent()).map(AbstractTypeClass::getRoot).orElse(typeClass);
    }

    @Override
    public Class<?> getInstanceClass() {
        if (rawClass.isInterface())
            return getDefaultInstanceClass();
        return rawClass;
    }

    @Override
    public <T> T newInstance() {
        try {
            return (T) getInstanceClass().newInstance();
        } catch (Throwable t) {
            throw ProtoUtils.wrapError(t);
        }
    }

    abstract Class<?> getDefaultInstanceClass();
}
