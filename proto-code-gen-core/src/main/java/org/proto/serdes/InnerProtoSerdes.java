package org.proto.serdes;

import com.google.protobuf.Descriptors;
import com.google.protobuf.MessageLite;
import org.proto.serdes.info.FieldInfo;
import org.proto.serdes.info.ProtoFieldInfo;
import org.proto.serdes.method.BeanFieldSetMethod;
import org.proto.serdes.method.SetMethodWrapper;
import org.proto.serdes.type.ListFieldClass;
import org.proto.serdes.type.MapFieldClass;
import org.proto.serdes.type.SetFieldClass;
import org.proto.serdes.type.TypeClass;

import java.lang.reflect.Method;
import java.util.*;

class InnerProtoSerdes<T> extends AbstractProtoSerdes<T> {
    private static final Map<Class<? extends TypeClass>, InnerMethodFinder> pojoSetMethodFinderMap = new HashMap<>();

    static {
        pojoSetMethodFinderMap.put(MapFieldClass.class, () -> new BeanFieldSetMethod(Map.class.getDeclaredMethod("putAll", Map.class)));
        pojoSetMethodFinderMap.put(SetFieldClass.class, () -> new BeanFieldSetMethod(Set.class.getDeclaredMethod("addAll", Collection.class)));
        pojoSetMethodFinderMap.put(ListFieldClass.class, () -> new BeanFieldSetMethod(List.class.getDeclaredMethod("addAll", Collection.class)));
    }

    private final InnerCodecField codecField;

    private InnerProtoSerdes(TypeClass pojoType, InnerCodecField codecField, Method parseFromMethod, Method newBuilderMethod) {
        super(pojoType, parseFromMethod, newBuilderMethod);
        this.codecField = codecField;
    }

    static <T> ProtoSerdes<T> create(ProtoInfoWrapper wrapper) throws Throwable {
        TypeClass pojoType = wrapper.getPojoType();
        Class<?> protoClass = wrapper.getProtoClass();
        List<Descriptors.FieldDescriptor> fieldDescriptors = ProtoUtils.getDescriptor(protoClass).getFields();
        if (fieldDescriptors.size() != 1)
            throw new RuntimeException("Invalid fieldDescriptors count, expected is 1, but now is: " + fieldDescriptors);//NOSONAR
        FieldInfo fieldInfo = new ProtoFieldInfo(1, fieldDescriptors.get(0).getName(), pojoType);
        InnerCodecField codecField = new InnerCodecField(
                fieldInfo,
                getProtoSetMethod(getProtoBuilderClass(protoClass), fieldInfo),
                getProtoGetMethod(protoClass, fieldInfo),
                findSetMethod(pojoType.getClass()),
                findValueConverter(fieldInfo)
        );
        return new InnerProtoSerdes<>(pojoType, codecField, ProtoUtils.getParseFromMethod(protoClass), ProtoUtils.getNewBuilderMethod(protoClass));
    }

    @Override
    public List<InnerCodecField> getCodecFields() {
        return Collections.singletonList(codecField);
    }

    @Override
    public MessageLite toProto(Object sPojo) {
        return toProtoHelper(sPojo, (protoObj, pojo) -> {
            Object newValue = codecField.valueConverter.apply(codecField, pojo, ProtoSerdes::toProto);
            codecField.protoSetMethod.setValue(protoObj, newValue);
        });
    }

    @Override
    public T toPojo(Object sProtoObj) {
        return toPojoHelper(sProtoObj, (protoObj, pojo) -> {
            Object oldValue = codecField.protoGetMethod.getValue(protoObj);
            Object newValue = codecField.valueConverter.apply(codecField, oldValue, ProtoSerdes::toPojo);
            findSetMethod(pojoType.getClass()).setValue(pojo, newValue);
        });
    }

    private static SetMethodWrapper findSetMethod(Class<? extends TypeClass> clazz) throws Throwable {
        return Optional.ofNullable(pojoSetMethodFinderMap.get(clazz))
                .orElseThrow(() -> new RuntimeException("No inner method finder found for: " + clazz))
                .apply();
    }

    interface InnerMethodFinder {
        SetMethodWrapper apply() throws Throwable;//NOSONAR
    }

}
