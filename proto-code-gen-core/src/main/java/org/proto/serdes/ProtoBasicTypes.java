package org.proto.serdes;

import com.google.protobuf.Descriptors.FieldDescriptor;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ProtoBasicTypes {
    private static final Map<Class<?>, FieldDescriptor.Type> class2ProtoType = new HashMap<>();
    private static final Map<Class<?>, Class<?>> class2ProtoPrimitive = new HashMap<>();
    private static final Map<Class<?>, Class<?>> primitive2Box = new HashMap<>();

    static {
        class2ProtoType.put(double.class, FieldDescriptor.Type.DOUBLE);
        class2ProtoType.put(Double.class, FieldDescriptor.Type.DOUBLE);
        class2ProtoType.put(float.class, FieldDescriptor.Type.FLOAT);
        class2ProtoType.put(Float.class, FieldDescriptor.Type.FLOAT);
        class2ProtoType.put(long.class, FieldDescriptor.Type.INT64);
        class2ProtoType.put(Long.class, FieldDescriptor.Type.INT64);
        class2ProtoType.put(int.class, FieldDescriptor.Type.INT32);
        class2ProtoType.put(Integer.class, FieldDescriptor.Type.INT32);
        class2ProtoType.put(short.class, FieldDescriptor.Type.INT32);
        class2ProtoType.put(Short.class, FieldDescriptor.Type.INT32);
        class2ProtoType.put(byte.class, FieldDescriptor.Type.INT32);
        class2ProtoType.put(Byte.class, FieldDescriptor.Type.INT32);
        class2ProtoType.put(char.class, FieldDescriptor.Type.INT32);
        class2ProtoType.put(Character.class, FieldDescriptor.Type.INT32);
        class2ProtoType.put(boolean.class, FieldDescriptor.Type.BOOL);
        class2ProtoType.put(Boolean.class, FieldDescriptor.Type.BOOL);
        class2ProtoType.put(String.class, FieldDescriptor.Type.STRING);

        class2ProtoPrimitive.put(Double.class, double.class);
        class2ProtoPrimitive.put(Float.class, float.class);
        class2ProtoPrimitive.put(Long.class, long.class);
        class2ProtoPrimitive.put(Integer.class, int.class);
        class2ProtoPrimitive.put(Boolean.class, boolean.class);
        class2ProtoPrimitive.put(Short.class, int.class);
        class2ProtoPrimitive.put(short.class, int.class);
        class2ProtoPrimitive.put(byte.class, int.class);
        class2ProtoPrimitive.put(Byte.class, int.class);
        class2ProtoPrimitive.put(Character.class, int.class);
        class2ProtoPrimitive.put(char.class, int.class);

        primitive2Box.put(double.class, Double.class);
        primitive2Box.put(float.class, Float.class);
        primitive2Box.put(long.class, Long.class);
        primitive2Box.put(int.class, Integer.class);
        primitive2Box.put(boolean.class, Boolean.class);
        primitive2Box.put(char.class, Character.class);
        primitive2Box.put(short.class, Short.class);
        primitive2Box.put(byte.class, Byte.class);
    }

    public static Optional<Class<?>> getBoxClass(Class<?> clazz) {
        return Optional.ofNullable(primitive2Box.get(clazz));
    }

    public static Optional<FieldDescriptor.Type> getFieldDescriptorType(Class<?> clazz) {
        return Optional.ofNullable(class2ProtoType.get(clazz));
    }

    public static Optional<Class<?>> getProtoPrimitive(Class<?> clazz) {
        return Optional.ofNullable(class2ProtoPrimitive.get(clazz));
    }

    public static boolean contains(Type type) {
        return type instanceof Class<?> && class2ProtoType.containsKey(type);
    }

    public static boolean contains(FieldDescriptor.Type type) {
        return class2ProtoType.containsValue(type);
    }

    static String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        AtomicInteger count = new AtomicInteger(0);
        class2ProtoType.keySet().forEach(key -> {
            if (count.get() > 0)
                sb.append(", ");
            sb.append(key.getName());
            count.addAndGet(1);
        });
        sb.append("]");
        return sb.toString();
    }
}
