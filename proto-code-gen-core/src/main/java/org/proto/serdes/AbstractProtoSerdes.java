package org.proto.serdes;

import com.google.protobuf.Message.Builder;
import com.google.protobuf.MessageLite;
import org.proto.serdes.info.FieldInfo;
import org.proto.serdes.method.BeanFieldGetMethod;
import org.proto.serdes.method.BeanFieldSetMethod;
import org.proto.serdes.method.GetMethodWrapper;
import org.proto.serdes.method.SetMethodWrapper;
import org.proto.serdes.transform.TransformMgr;
import org.proto.serdes.type.*;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public abstract class AbstractProtoSerdes<T> implements ProtoSerdes<T> {
    private static Map<Class<? extends TypeClass>, MethodFinder<SetMethodWrapper>> protoSetMethodFinderMap = new HashMap<>();
    private static Map<Class<? extends TypeClass>, MethodFinder<GetMethodWrapper>> protoGetMethodFinderMap = new HashMap<>();

    static {
        protoSetMethodFinderMap.put(FieldClass.class, (protoClass, fieldInfo) -> {
            String methodName = ProtoUtils.getBeanMethodName("set", fieldInfo.getProtoField());
            Class<?> argClass = fieldInfo.getTypeClass().getProtoClassName()
                    .<Class>map(ProtoUtils::newProtoClass)
                    .orElse(fieldInfo.getTypeClass().getRawClass());
            if (ProtoBasicTypes.contains(argClass)) {
                argClass = ProtoBasicTypes.getProtoPrimitive(argClass).orElse(argClass);
            }
            return new BeanFieldSetMethod(protoClass.getDeclaredMethod(methodName, argClass));
        });
        protoSetMethodFinderMap.put(MapFieldClass.class, (protoClass, fieldInfo) -> {
            String methodName = ProtoUtils.getBeanMethodName("putAll", fieldInfo.getProtoField());
            return new BeanFieldSetMethod(protoClass.getDeclaredMethod(methodName, Map.class));
        });
        MethodFinder<SetMethodWrapper> protoCollectionSetFinder = (protoClass, fieldInfo) -> {
            String methodName = ProtoUtils.getBeanMethodName("addAll", fieldInfo.getProtoField());
            return new BeanFieldSetMethod(protoClass.getDeclaredMethod(methodName, Iterable.class));
        };
        protoSetMethodFinderMap.put(SetFieldClass.class, protoCollectionSetFinder);
        protoSetMethodFinderMap.put(ListFieldClass.class, protoCollectionSetFinder);


        protoGetMethodFinderMap.put(FieldClass.class, (protoClass, fieldInfo) -> {
            String methodName = ProtoUtils.getBeanMethodName("get", fieldInfo.getProtoField());
            return new BeanFieldGetMethod(protoClass.getDeclaredMethod(methodName));
        });
        protoGetMethodFinderMap.put(MapFieldClass.class, (protoClass, fieldInfo) -> {
            String methodName = ProtoUtils.getBeanMethodName("get", fieldInfo.getProtoField() + "Map");
            return new BeanFieldGetMethod(protoClass.getDeclaredMethod(methodName));
        });
        MethodFinder<GetMethodWrapper> protoCollectionGetFinder = (protoClass, fieldInfo) -> {
            String methodName = ProtoUtils.getBeanMethodName("get", fieldInfo.getProtoField()) + "List";
            return new BeanFieldGetMethod(protoClass.getDeclaredMethod(methodName));
        };
        protoGetMethodFinderMap.put(SetFieldClass.class, protoCollectionGetFinder);
        protoGetMethodFinderMap.put(ListFieldClass.class, protoCollectionGetFinder);
    }

    private static final Map<Class<? extends TypeClass>, ValueConverter> valueConverterMap = new HashMap<>();
    private static final ValueConverter defaultValueConverter = new DefaultValueConverter();

    static {
        valueConverterMap.put(SetFieldClass.class, AbstractProtoSerdes::newCollectionFunc);
        valueConverterMap.put(ListFieldClass.class, AbstractProtoSerdes::newCollectionFunc);
        valueConverterMap.put(MapFieldClass.class, AbstractProtoSerdes::newMapFunc);
    }

    private static Collection newCollectionFunc(final TypeClass fieldTypeClass, final Object oldValue, final ConvertFunc convertFunc, final TransformMgr.FuncCache funcCache) {
        CollectionFieldClass typeClass = (CollectionFieldClass) fieldTypeClass;
        Collection newValue = typeClass.newInstance();
        Collection oldCollection = (Collection) oldValue;
        TypeClass valueType = typeClass.getValueType();
        oldCollection.forEach(v ->
                newValue.add(
                        defaultValueConverter.apply(valueType, v, convertFunc, funcCache)
                )
        );
        return newValue;
    }

    private static Map newMapFunc(final TypeClass fieldTypeClass, final Object oldValue, final ConvertFunc convertFunc, final TransformMgr.FuncCache funcCache) {
        final Map oldMap = (Map) oldValue;
        MapFieldClass typeClass = (MapFieldClass) fieldTypeClass;
        final Map newMap = typeClass.newInstance();
        TypeClass valueType = typeClass.getValueType();
        oldMap.forEach((oKey, oValue) ->
                newMap.put(
                        oKey,
                        defaultValueConverter.apply(valueType, oValue, convertFunc, funcCache)
                )
        );
        return newMap;
    }

    static ValueConverter findValueConverter(FieldInfo fieldInfo) {
        ValueConverter valueConverter = valueConverterMap.get(fieldInfo.getTypeClass().getClass());
        return valueConverter == null ? defaultValueConverter : valueConverter;
    }

    final TypeClass pojoType;
    private final Method parseFromMethod;
    private final Method newBuilderMethod;

    static Class<?> getProtoBuilderClass(Class<?> protoClass) {
        return Stream.of(protoClass.getDeclaredClasses())
                .filter(Builder.class::isAssignableFrom)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No Builder class found in: " + protoClass));
    }

    AbstractProtoSerdes(TypeClass pojoType, Method parseFromMethod, Method newBuilderMethod) {
        this.pojoType = pojoType;
        this.parseFromMethod = parseFromMethod;
        this.newBuilderMethod = newBuilderMethod;
    }

    MessageLite toProtoHelper(Object pojo, BuildFunc func) {
        try {
            if (!pojoType.getRawClass().isAssignableFrom(pojo.getClass()))
                throw new RuntimeException("Invalid object type: " + pojo.getClass() + ", expected: " + pojoType.getRawClass());//NOSONAR
            Object protoBuilderObj = newBuilderMethod.invoke(null);
            if (!(protoBuilderObj instanceof Builder))
                throw new RuntimeException(protoBuilderObj + " is not an instance of " + Builder.class.getName());//NOSONAR
            Builder builder = (Builder) protoBuilderObj;
            func.apply(builder, pojo);
            return builder.build();
        } catch (Throwable t) {
            throw ProtoUtils.wrapError(t);
        }
    }

    @SuppressWarnings("unchecked")
    T toPojoHelper(Object protoObj, BuildFunc func) {
        if (!(protoObj instanceof MessageLite)) {
            throw new RuntimeException(protoObj + " is not a proto buffer object.");
        }
        try {
            T pojo = pojoType.newInstance();
            func.apply(protoObj, pojo);
            return pojo;
        } catch (Throwable t) {
            throw ProtoUtils.wrapError(t);
        }
    }

    public byte[] serialize(T pojo) {
        return toProto(pojo).toByteArray();
    }

    public T deserialize(byte[] bs) {
        try {
            return toPojo(parseFromMethod.invoke(null, bs));
        } catch (Exception t) {
            throw ProtoUtils.wrapError(t);
        }
    }

    private static <T> T getProtoMethod(Map<Class<? extends TypeClass>, MethodFinder<T>> methodFinderMap, Class<?> protoClass, FieldInfo fieldInfo) throws Throwable {
        return Optional.ofNullable(methodFinderMap.get(fieldInfo.getTypeClass().getClass()))
                .orElseThrow(() -> new RuntimeException("No method finder found by: " + fieldInfo))
                .find(protoClass, fieldInfo);
    }

    static SetMethodWrapper getProtoSetMethod(Class<?> protoClass, FieldInfo fieldInfo) throws Throwable {
        return getProtoMethod(protoSetMethodFinderMap, protoClass, fieldInfo);
    }

    static GetMethodWrapper getProtoGetMethod(Class<?> protoClass, FieldInfo fieldInfo) throws Throwable {
        return getProtoMethod(protoGetMethodFinderMap, protoClass, fieldInfo);
    }

    interface BuildFunc {
        void apply(Object protoObj, Object pojo) throws Throwable;//NOSONAR
    }

    interface MethodFinder<T> {
        T find(Class<?> protoClass, FieldInfo fieldInfo) throws Throwable;//NOSONAR
    }


}
