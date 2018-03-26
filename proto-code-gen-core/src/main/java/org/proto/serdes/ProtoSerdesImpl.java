package org.proto.serdes;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.MessageLite;
import org.proto.serdes.info.FieldInfo;
import org.proto.serdes.method.*;
import org.proto.serdes.transform.TransformMgr;
import org.proto.serdes.type.TypeClass;
import org.proto.serdes.utils.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class ProtoSerdesImpl<T> extends AbstractProtoSerdes<T> {

    private final List<ProtoCodecField> codecFields;

    private ProtoSerdesImpl(TypeClass pojoType, List<ProtoCodecField> codecFields,
                            Method parseFromMethod, Method newBuilderMethod) throws Throwable {
        super(pojoType, parseFromMethod, newBuilderMethod);
        this.codecFields = codecFields;
    }

    @Override
    public List<ProtoCodecField> getCodecFields() {
        return new ArrayList<>(codecFields);
    }

    @Override
    public MessageLite toProto(Object sPojo) {
        return toProtoHelper(sPojo, (protoObj, pojo) -> codecFields.forEach(field -> populateProtoField(protoObj, pojo, field, ProtoSerdes::toProto)));
    }

    @Override
    public T toPojo(Object sProtoObj) {
        return toPojoHelper(sProtoObj, (protoObj, pojo) -> codecFields.forEach(field -> populatePojoField(protoObj, pojo, field, ProtoSerdes::toPojo)));
    }

    private void populateProtoField(Object protoObj, Object pojo, ProtoCodecField codecField, ConvertFunc func) {
        populateField(codecField, pojo, codecField.pojoGetMethod, protoObj, codecField.protoSetMethod, func, TransformMgr.toProtoCache);
    }

    private void populatePojoField(Object protoObj, Object pojo, ProtoCodecField codecField, ConvertFunc func) {
        populateField(codecField, protoObj, codecField.protoGetMethod, pojo, codecField.pojoSetMethod, func, TransformMgr.toPojoCache);
    }

    private void populateField(CodecField codecField, Object source, GetMethodWrapper sourceGetMethod,
                               Object target, SetMethodWrapper targetSetMethod, ConvertFunc convertFunc, TransformMgr.FuncCache funcCache) {
        Object convertedValue = null;
        try {
            Object value = sourceGetMethod.getValue(source);
            if (value != null) {
                convertedValue = funcCache.get(codecField.getType())
                        .map(func -> func.transform(value))
                        .orElseGet(() -> {
                            try {
                                return codecField.valueConverter.apply(codecField, value, convertFunc);
                            } catch (Throwable t) {
                                throw ProtoUtils.wrapError(t);
                            }
                        });
                targetSetMethod.setValue(target, convertedValue);
            }
        } catch (Throwable t) {
            throw new RuntimeException(
                    "populate field fail: "
                            + "\nfield class: " + codecField.getClass()
                            + "\nfieldName: " + codecField.getName()
                            + "\nget method: " + sourceGetMethod
                            + "\nset method: " + targetSetMethod
                            + "\nvalue type: " + (convertedValue == null ? null : convertedValue.getClass())
                            + "\nvalue: " + convertedValue,
                    t
            );
        }
    }

    static <T> ProtoSerdes<T> create(ProtoInfoWrapper wrapper) throws Throwable {
        TypeClass pojoType = wrapper.getPojoType();
        Class<?> protoClass = wrapper.getProtoClass();
        return new ProtoSerdesImpl<>(pojoType,
                getProtoCodecFields(pojoType, protoClass, ProtoUtils.getDescriptor(protoClass), getProtoBuilderClass(protoClass), wrapper.getFieldPairs()),
                ProtoUtils.getParseFromMethod(protoClass),
                ProtoUtils.getNewBuilderMethod(protoClass));
    }

    private static List<ProtoCodecField> getProtoCodecFields(TypeClass pojoType, Class<?> protoClass, Descriptor descriptor,
                                                             Class<?> protoBuilderClass, List<Pair<Field, FieldInfo>> fieldPairs) throws Throwable {
        final Map<String, FieldDescriptor> fieldDescriptorMap = descriptor.getFields()
                .stream()
                .collect(Collectors.toMap(FieldDescriptor::getName, fd -> fd));
        return fieldPairs.stream().map(pair -> newProtoCodecField(pair.left, pair.right, pojoType, protoClass, protoBuilderClass, fieldDescriptorMap))
                .collect(Collectors.toList());
    }

    private static ProtoCodecField newProtoCodecField(Field pojoField, FieldInfo fieldInfo, TypeClass pojoType, Class<?> protoClass,
                                                      Class<?> protoBuilderClass, Map<String, FieldDescriptor> fieldDescriptorMap) {
        try {
            FieldDescriptor fieldDescriptor = fieldDescriptorMap.get(fieldInfo.getProtoField());
            if (fieldDescriptor == null)
                throw new RuntimeException("No proto buffer pojoField descriptor found: " + fieldInfo.getProtoField());
            checkField(pojoField, fieldInfo, fieldDescriptor);
            TypeClass fieldType = fieldInfo.getTypeClass();
            if (!ProtoBasicTypes.contains(fieldDescriptor.getType()))
                fieldType.compute(fieldDescriptor.getMessageType());
            return new ProtoCodecField(pojoField,
                    fieldInfo,
                    getPojoSetMethod(pojoType.getRawClass(), pojoField),
                    getPojoGetMethod(pojoType.getRawClass(), pojoField),
                    getProtoSetMethod(protoBuilderClass, fieldInfo),
                    getProtoGetMethod(protoClass, fieldInfo),
                    findValueConverter(fieldInfo)
            );
        } catch (Throwable t) {
            throw ProtoUtils.wrapError(t);
        }
    }

    private static void checkField(Field pojoField, FieldInfo fieldInfo, FieldDescriptor fieldDescriptor) {
        if (!fieldInfo.getProtoField().equals(fieldDescriptor.getName()))
            throw newCheckError(pojoField.getName(), "proto name", fieldInfo.getProtoField(), fieldDescriptor.getName());
        if (fieldInfo.getIndex() != fieldDescriptor.getNumber())
            throw newCheckError(pojoField.getName(), "proto index", fieldInfo.getIndex(), fieldDescriptor.getNumber());
    }

    private static RuntimeException newCheckError(Object... args) {
        if (args.length != 4)
            throw new IllegalArgumentException("Invalid arguments count, expect 4 arguments");
        String errMsg = MessageFormat.format("field {0} with different {1}, annotation is {2} and proto file is {3}", args);
        throw new RuntimeException(errMsg);
    }

    private static GetMethodWrapper getPojoGetMethod(Class<?> pojoClass, Field pojoField) throws Throwable {
        if (ProtoUtils.isBeanField(pojoField))
            return new BeanFieldGetMethod(pojoClass.getDeclaredMethod(ProtoUtils.getBeanMethodName("get", pojoField.getName())));
        else if (Modifier.isPublic(pojoField.getModifiers()))
            return new DirectFieldGetMethod(pojoField);
        throw new RuntimeException(pojoField + " is not public or a bean field.");
    }

    private static SetMethodWrapper getPojoSetMethod(Class<?> pojoClass, Field pojoField) throws Throwable {
        if (ProtoUtils.isBeanField(pojoField))
            return new BeanFieldSetMethod(pojoClass.getDeclaredMethod(ProtoUtils.getBeanMethodName("set", pojoField.getName()), pojoField.getType()));
        else if (Modifier.isPublic(pojoField.getModifiers()))
            return new DirectFieldSetMethod(pojoField);
        throw new RuntimeException(pojoField + " is not public or a bean field.");
    }
}
