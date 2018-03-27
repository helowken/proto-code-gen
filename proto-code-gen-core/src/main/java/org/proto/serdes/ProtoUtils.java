package org.proto.serdes;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import org.proto.serdes.annotation.ProtoClass;
import org.proto.serdes.annotation.ProtoField;
import org.proto.serdes.error.*;
import org.proto.serdes.info.FieldInfo;
import org.proto.serdes.info.ProtoClassInfo;
import org.proto.serdes.info.ProtoFieldInfo;
import org.proto.serdes.type.*;
import org.proto.serdes.utils.Pair;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

public class ProtoUtils {
    public static RuntimeException wrapError(Throwable t) {
        if (t instanceof RuntimeException)
            return (RuntimeException) t;
        return new RuntimeException(t);
    }

    public static Class<?> loadClass(String name) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(name);
        } catch (Exception e) {
            throw ProtoUtils.wrapError(e);
        }
    }

    @SuppressWarnings("unchecked")
    static Class<? extends MessageLite> newProtoClass(String protoClassName) {
        return (Class<? extends MessageLite>) loadClass(ProtoConst.DEFAULT_JAVA_PACKAGE + "." + protoClassName);
    }

    private static String getProtoFieldName(Field pojoField, String fieldName) {
        String protoFieldName = fieldName;
        if (protoFieldName.trim().isEmpty()) {
            protoFieldName = pojoField.getName();
        }
        return protoFieldName;
    }

    private static FieldInfo createFieldInfo(Class<?> pojoClass, final Field pojoField, String protoFieldName, int protoIndex) {
        try {
            TypeClass typeClass = createTypeClass(pojoField.getGenericType());
            validateTypeClass(typeClass, pojoField);
            return new ProtoFieldInfo(protoIndex, protoFieldName, typeClass);
        } catch (NoArgTypeClassFoundException t) {
            throw new NoArgTypeClassFoundException(errorMsg(pojoClass, pojoField, t.getMessage()), t);
        }
    }

    private static Optional<FieldInfo> createFieldInfo(final Class<?> pojoClass, final Field pojoField) {
        return Optional.ofNullable(pojoField.getAnnotation(ProtoField.class))
                .map(annt -> createFieldInfo(pojoClass, pojoField, getProtoFieldName(pojoField, annt.field()), annt.index()));
    }

    private static void validateTypeClass(TypeClass typeClass, Field pojoField) {
        Set<Class<?>> rawClasses = new HashSet<>();
        collectAllRawClasses(typeClass, rawClasses);
        rawClasses.stream()
                .filter(clazz -> !isMap(clazz) && !isIterable(clazz))
                .forEach(clazz -> {
                    if (!ProtoBasicTypes.contains(clazz)
                            && !hasProtoClassAnnt(clazz)
                            && !ProtoSerdesFactory.hasConfig(clazz))
                        throw new InvalidTypeClassException(errorMsg(typeClass.getRawClass(), pojoField,
                                clazz + " is not a primitive type " + ProtoBasicTypes.getDescription() + " or another Proto class"));
                });
    }

    private static void collectAllRawClasses(TypeClass typeClass, Set<Class<?>> rawClasses) {
        rawClasses.add(typeClass.getRawClass());
        typeClass.getChildren().forEach(subType -> collectAllRawClasses(subType, rawClasses));
    }

    static TypeClass createTypeClass(Type type) {
        return createTypeClass(type, 0);
    }

    private static TypeClass createTypeClass(Type type, int level) {
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type[] argTypes = pType.getActualTypeArguments();
            Type rawType = pType.getRawType();
            if (!(rawType instanceof Class))
                throw new RuntimeException("Unsupported raw type: " + rawType);//NOSONAR
            Class<?> rawClass = (Class<?>) rawType;
            boolean inner = level > 0;
            if (isMap(rawClass)) {
                if (argTypes.length != 2)
                    throw new RuntimeException("Invalid arg types count for map: " + argTypes.length);//NOSONAR
                return new MapFieldClass(inner, createTypeClass(argTypes[0], level + 1), createTypeClass(argTypes[1], level + 1));
            } else if (isIterable(rawClass)) {
                if (argTypes.length != 1)
                    throw new RuntimeException("Invalid arg types count for Collection: " + argTypes.length);//NOSONAR
                if (isList(rawClass))
                    return new ListFieldClass(rawClass, inner, createTypeClass(argTypes[0], level + 1));
                else if (isSet(rawClass))
                    return new SetFieldClass(rawClass, inner, createTypeClass(argTypes[0], level + 1));
            }
            throw new RuntimeException("Unsupported parameterized type: " + type);//NOSONAR
        } else if (type instanceof Class) {
            Class<?> clazz = (Class<?>) type;
            if (ProtoUtils.isMap(clazz) || ProtoUtils.isIterable(clazz))
                throw new NoArgTypeClassFoundException("Map or Collection type must has argument type(s).");
            return new FieldClass(clazz);
        } else
            throw new RuntimeException("Unknown type: " + type);//NOSONAR
    }

    public static Optional<ProtoClassInfo> getProtoClassInfo(Class<?> pojoClass) {
        Optional<ProtoClassInfo> opt = getProtoClassInfoFromAnnt(pojoClass);
        if (!opt.isPresent())
            opt = ProtoSerdesFactory.getConfig(pojoClass)
                    .map(wrapper -> new ProtoClassInfo(wrapper.protoClassName));
        return opt;
    }

    private static Optional<ProtoClassInfo> getProtoClassInfoFromAnnt(Class<?> pojoClass) {
        return Optional.ofNullable(pojoClass.getAnnotation(ProtoClass.class))
                .map(annt -> new ProtoClassInfo(annt.protoClass()));
    }

    public static boolean hasProtoClassAnnt(Class<?> pojoClass) {
        return getProtoClassInfoFromAnnt(pojoClass).isPresent();
    }

    private static List<Pair<Field, FieldInfo>> getProtoFieldPairs(Class<?> pojoClass, List<FieldWrapper> fieldWrappers) {
        List<Pair<Field, FieldInfo>> pairs = new ArrayList<>();
        for (FieldWrapper fieldWrapper : fieldWrappers) {
            pairs.add(new Pair<>(
                            fieldWrapper.field,
                            createFieldInfo(
                                    pojoClass,
                                    fieldWrapper.field,
                                    fieldWrapper.protoFieldName,
                                    fieldWrapper.index
                            )
                    )
            );
        }
        checkFieldInfos(pojoClass, pairs);
        return pairs;
    }

    public static List<Pair<Field, FieldInfo>> getProtoFieldPairs(Class<?> pojoClass) {
        List<Pair<Field, FieldInfo>> stream = FieldsCollector.getFields(pojoClass)
                .stream()
                .map(field -> createFieldInfo(pojoClass, field).map(info -> new Pair<>(field, info)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        checkFieldInfos(pojoClass, stream);
        return stream;
    }

    private static void checkFieldInfos(Class<?> pojoClass, List<Pair<Field, FieldInfo>> stream) {
        if (stream.isEmpty())
            throw new RuntimeException("Fields can not be empty, pojo Class: " + pojoClass);
        Set<Integer> indexes = new HashSet<>();
        Set<String> fieldNames = new HashSet<>();
        stream.forEach(pair -> {
            Field field = pair.left;
            if (!isValidField(field))
                throw new InvalidFieldException(errorMsg(pojoClass, field, "is not a public field or a bean field."));
            FieldInfo fieldInfo = pair.right;
            if (indexes.contains(fieldInfo.getIndex()))
                throw new DuplicatedFieldIndexException(errorMsg(pojoClass, field, "Duplicated index: " + fieldInfo.getIndex()));
            indexes.add(fieldInfo.getIndex());
            if (fieldNames.contains(fieldInfo.getProtoField()))
                throw new DuplicatedFieldNameException(errorMsg(pojoClass, field, "Duplicated proto field: " + fieldInfo.getProtoField()));
            fieldNames.add(fieldInfo.getProtoField());
        });
    }

    static ProtoInfoWrapper createWrapper(TypeClass pojoType) {
        try {
            return new ProtoInfoWrapper(
                    pojoType,
                    pojoType.getProtoClassNameOrError(),
                    () -> pojoType.isInner() ? Collections.emptyList() : getProtoFieldPairs(pojoType.getRawClass())
            );
        } catch (Throwable t) {
            throw wrapError(t);
        }
    }

    public static ProtoInfoWrapper createWrapper(Class<?> pojoClass, String protoClassName, final List<FieldWrapper> fieldWrappers) {
        try {
            return new ProtoInfoWrapper(
                    createTypeClass(pojoClass),
                    protoClassName,
                    () -> getProtoFieldPairs(pojoClass, fieldWrappers)
            );
        } catch (Throwable t) {
            throw wrapError(t);
        }
    }

    private static String errorMsg(Class<?> pojoClass, Field field, String errMsg) {
        return "Pojo class: " + pojoClass + ", Field: " + field.getName() + ", error: " + errMsg;
    }

    public static boolean isMap(Class<?> clazz) {
        return isInterface(clazz, Map.class);
    }

    private static boolean isList(Class<?> clazz) {
        return isInterface(clazz, List.class);
    }

    private static boolean isSet(Class<?> clazz) {
        return isInterface(clazz, Set.class);
    }

    public static boolean isIterable(Class<?> clazz) {
        return isInterface(clazz, Iterable.class);
    }

    private static boolean isInterface(Class<?> clazz, Class<?> targetInterface) {
        return clazz == targetInterface || targetInterface.isAssignableFrom(clazz);
    }

    static String getBeanMethodName(String prefix, String fieldName) {
        return prefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    static boolean isBeanField(Field field) {
        try {
            String fieldName = field.getName();
            Class<?> fieldType = field.getType();
            String setMethodName = getBeanMethodName("set", fieldName);
            Class<?> clazz = field.getDeclaringClass();
            clazz.getDeclaredMethod(setMethodName, fieldType);

            String getMethodName = getBeanMethodName("get", fieldName);
            if (fieldType == boolean.class || fieldType == Boolean.class) {
                try {
                    clazz.getDeclaredMethod(getMethodName, fieldType);
                } catch (Throwable t) {
                    getMethodName = getBeanMethodName("is", fieldName);
                    clazz.getDeclaredMethod(getMethodName, fieldType);
                }
            }
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    static Descriptors.Descriptor getDescriptor(Class<?> protoClass) throws Throwable {//NOSONAR
        Message.Builder builder = (Message.Builder) getNewBuilderMethod(protoClass).invoke(null);
        return builder.getDescriptorForType();
    }

    static Method getParseFromMethod(Class<?> protoClass) throws Throwable {//NOSONAR
        return protoClass.getMethod(ProtoConst.PARSE_FROM_METHOD, byte[].class);
    }

    static Method getNewBuilderMethod(Class<?> protoClass) throws Throwable {//NOSONAR
        return protoClass.getMethod(ProtoConst.NEW_BUILDER_METHOD);
    }

    static boolean isValidField(Field field) {
        int modifiers = field.getModifiers();
        return !Modifier.isFinal(modifiers)
                && !Modifier.isStatic(modifiers)
                && (Modifier.isPublic(modifiers) || isBeanField(field));
    }
}
