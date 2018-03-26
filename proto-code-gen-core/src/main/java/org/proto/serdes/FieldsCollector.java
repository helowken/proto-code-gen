package org.proto.serdes;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;
import org.proto.serdes.utils.Pair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FieldsCollector {
    public static List<Field> getFields(Class<?> clazz) {
        return Stream.of(clazz.getDeclaredFields())
                .filter(ProtoUtils::isValidField)
                .collect(Collectors.toList());
    }

    public static List<FieldWrapper> getFieldWrappers(Class<?> clazz) {
        AtomicInteger index = new AtomicInteger(0);
        return getFields(clazz).stream()
                .map(field -> new FieldWrapper(field, index.addAndGet(1), field.getName()))
                .collect(Collectors.toList());
    }

    public static List<FieldWrapper> getFieldWrappers(Class<?> pojoClass, Class<? extends MessageLite> protoClass) {
        final Map<String, Field> protoField2PojoField = new HashMap<>();
        getFields(pojoClass).forEach(field -> protoField2PojoField.put(field.getName(), field));
        return getFieldWrappers(protoClass, protoField2PojoField, (field, pair) -> new FieldWrapper(field, pair.left, pair.right));
    }

    public static List<FieldWrapper> getFieldWrappers(Class<? extends Message> protoClass, Map<String, Field> protoField2PojoField) {
        return getFieldWrappers(protoClass, protoField2PojoField, (field, pair) -> new FieldWrapper(field, pair.left, pair.right));
    }

    public static List<FieldWrapper> getFieldWrappers(Class<? extends MessageLite> protoClass, Map<String, Field> protoField2PojoField,
                                                      BiFunction<Field, Pair<Integer, String>, FieldWrapper> func) {
        try {
            Map<String, FieldDescriptor> name2FieldDescriptor = new HashMap<>();
            AtomicInteger maxIndex = new AtomicInteger(0);
            ProtoUtils.getDescriptor(protoClass).getFields().forEach(fieldDescriptor -> {
                name2FieldDescriptor.put(fieldDescriptor.getName(), fieldDescriptor);
                maxIndex.set(Math.max(fieldDescriptor.getNumber(), maxIndex.get()));
            });
            List<FieldWrapper> rsList = new ArrayList<>();
            Map<String, Field> newFieldMap = new HashMap<>();
            protoField2PojoField.forEach((protoFieldName, field) -> {
                FieldDescriptor fieldDescriptor = name2FieldDescriptor.remove(protoFieldName);
                if (fieldDescriptor == null)
                    newFieldMap.put(protoFieldName, field);
                else
                    rsList.add(new FieldWrapper(field, fieldDescriptor.getNumber(), protoFieldName));
            });
            if (!name2FieldDescriptor.isEmpty()) {
                throw new RuntimeException("Old proto fields left: " + name2FieldDescriptor.keySet());
            }
            if (!newFieldMap.isEmpty()) {
                newFieldMap.forEach((protoFieldName, field) -> {
                    FieldWrapper fieldWrapper = func.apply(field, new Pair<>(maxIndex.addAndGet(1), protoFieldName));
                    rsList.add(fieldWrapper);
                    maxIndex.set(fieldWrapper.index);
                });
            }
            return rsList;
        } catch (Throwable t) {
            throw ProtoUtils.wrapError(t);
        }
    }
}
