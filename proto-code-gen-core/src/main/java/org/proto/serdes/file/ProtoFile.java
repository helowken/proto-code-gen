package org.proto.serdes.file;

import org.proto.serdes.*;
import org.proto.serdes.info.FieldInfo;
import org.proto.serdes.utils.MultiRowStrings;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ProtoFile {
    private static AtomicInteger mapId = new AtomicInteger(0);
    private static AtomicInteger repeatedId = new AtomicInteger(0);

    private static String newInnerMapName() {
        return "_InnerMap" + mapId.addAndGet(1);
    }

    private static String newInnerRepeatedName() {
        return "_InnerRepeated" + repeatedId.addAndGet(1);
    }

    private String syntax;
    private String protoPackage;
    private String javaPackage;
    private String outerClassName;
    private boolean multipleFiles;
    private final Set<Class<?>> pojoClasses = new HashSet<>();
    private final Set<Message> messages = new HashSet<>();
    private final Set<Message> innerMessages = new HashSet<>();
    private final Map<FieldSymbol, FieldSymbol> innerFieldMap = new HashMap<>();
    //    private final Map<Class<?>, Set<Class<?>>> traceMap = new HashMap<>();
    private boolean built = false;

    public ProtoFile(String protoPackage, String outerClassName, Collection<Class<?>> pojoClasses) {
        this("proto3", protoPackage, ProtoConst.DEFAULT_JAVA_PACKAGE, outerClassName, true, pojoClasses);
    }

    private ProtoFile(String syntax, String protoPackage, String javaPackage, String outerClassName, boolean multipleFiles, Collection<Class<?>> pojoClasses) {
        this.syntax = syntax;
        this.protoPackage = protoPackage;
        this.javaPackage = javaPackage;
        this.outerClassName = outerClassName;
        this.multipleFiles = multipleFiles;
        this.pojoClasses.addAll(pojoClasses);
    }

    private synchronized void build() {
        if (!built) {
            pojoClasses.forEach(this::buildClass);
            built = true;
        }
    }

    private ProtoInfoWrapper buildClass(Class<?> pojoClass) {
//        Set<Class<?>> containedClasses = traceMap.computeIfAbsent(pojoClass, key -> new HashSet<>());
//        containedClasses.add(pojoClass);

        ProtoInfoWrapper wrapper = ProtoSerdesFactory.getInstance().getOrCreateProtoInfoWrapper(pojoClass);
        messages.add(new Message(wrapper.protoClassName, buildFields(wrapper)));
        return wrapper;
    }

    private List<Message.MessageField> buildFields(ProtoInfoWrapper wrapper) {
        return wrapper.getFieldPairs().stream()
                .map(pair -> {
                    Field pojoField = pair.left;
                    FieldInfo fieldInfo = pair.right;
                    try {
                        FieldSymbol fieldSymbol = newFieldSymbol(pojoField.getGenericType());
                        innerMessages.addAll(fieldSymbol.getInnerMessages());
                        return new Message.MessageField(fieldInfo.getProtoField(), fieldSymbol, fieldInfo.getIndex());
                    } catch (Throwable t) {
                        throw new RuntimeException(wrapper.getPojoType() + " pojo field " + pojoField.getName() + " has error: " + t.getMessage(), t);
                    }
                })
                .collect(Collectors.toList());
    }

    public String getContent() {
        build();
        MultiRowStrings rows = new MultiRowStrings("\n");
        rows.add(createHeaders().toString());
        rows.add(createOptions().toString());
        innerMessages.stream().sorted(Message.comparator).forEach(msg -> rows.add(msg.toString()));
        messages.stream().sorted(Message.comparator).forEach(msg -> rows.add(msg.toString()));
        return rows.toString();
    }

    private MultiRowStrings createHeaders() {
        MultiRowStrings rows = new MultiRowStrings(";\n");
        rows.add("syntax = \"" + syntax + "\"");
        rows.add("package " + protoPackage);
        return rows;
    }

    private MultiRowStrings createOptions() {
        MultiRowStrings rows = new MultiRowStrings(";\n");
        rows.add("option java_package = \"" + javaPackage + "\"");
        rows.add("option java_outer_classname = \"" + outerClassName + "\"");
        rows.add("option java_multiple_files = " + multipleFiles);
        return rows;
    }

    private FieldSymbol newFieldSymbol(Type type) {
        return newFieldSymbol(type, 0);
    }

    private FieldSymbol newFieldSymbol(Type type, int level) {
        FieldSymbol fieldSymbol;
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type rawType = pType.getRawType();
            if (!(rawType instanceof Class))
                throw new RuntimeException("Unsupported raw type: " + rawType + ", class: " + rawType.getClass());
            fieldSymbol = newFieldSymbolByClass((Class<?>) pType.getRawType(), level);
            fieldSymbol.setActualArgTypes(pType.getActualTypeArguments());
        } else if (type instanceof Class) {
            Class<?> clazz = (Class<?>) type;
            if (ProtoUtils.isMap(clazz) || ProtoUtils.isIterable(clazz))
                throw new RuntimeException("Map or Collection type must has argument type(s).");
            fieldSymbol = newFieldSymbolByClass(clazz, level);
        } else if (type instanceof WildcardType) {
            throw new RuntimeException("Wildcard type is not supported is proto");
        } else {
            throw new RuntimeException("Unsupported type: " + type + ", " + type.getClass());
        }
        return innerFieldMap.computeIfAbsent(fieldSymbol, key -> fieldSymbol);
    }

    private FieldSymbol newFieldSymbolByClass(Class<?> clazz, int level) {
        return ProtoBasicTypes.getFieldDescriptorType(clazz)
                .map(fdt -> (FieldSymbol) new LiteralField(level, fdt.name().toLowerCase()))
                .orElseGet(() -> {
                    if (ProtoUtils.isMap(clazz)) {
                        final FieldSymbol mapType = new MapField(level);
                        return level == 0 ? mapType : new InnerField(ProtoFile::newInnerMapName, mapType);
                    } else if (ProtoUtils.isIterable(clazz)) {
                        FieldSymbol repeatedType = new RepeatedField(level);
                        return level == 0 ? repeatedType : new InnerField(ProtoFile::newInnerRepeatedName, repeatedType);
                    }
                    return new LiteralField(level, buildClass(clazz).protoClassName);
                });
    }

    private abstract class AbstractFieldSymbol implements FieldSymbol {
        private int level;

        AbstractFieldSymbol(int level) {
            this.level = level;
        }

        @Override
        public int getLevel() {
            return level;
        }

        public String toString() {
            return getTypeString();
        }
    }

    interface InnerFieldNameFunc {
        String apply();
    }

    private class InnerField extends AbstractFieldSymbol {
        private InnerFieldNameFunc nameFunc;
        private FieldSymbol refType;
        private String name;

        InnerField(InnerFieldNameFunc nameFunc, FieldSymbol refType) {
            super(refType.getLevel());
            this.nameFunc = nameFunc;
            this.refType = refType;
        }

        @Override
        public String getTypeString() {
            return getName();
        }

        private String getName() {
            if (name == null)
                name = nameFunc.apply();
            return name;
        }

        @Override
        public void setActualArgTypes(Type[] argTypes) {
            refType.setActualArgTypes(argTypes);
        }

        @Override
        public Set<Message> getInnerMessages() {
            Message.MessageField field = new Message.MessageField(ProtoConst.INNER_DEFAULT_FIELD_NAME, refType, 1);
            Message innerMessage = new Message(getName(), Collections.singletonList(field));
            Set<Message> innerMessages = new HashSet<>();
            innerMessages.add(innerMessage);
            innerMessages.addAll(refType.getInnerMessages());
            return innerMessages;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            InnerField that = (InnerField) o;

            return refType != null ? refType.equals(that.refType) : that.refType == null;
        }

        @Override
        public int hashCode() {
            return refType != null ? refType.hashCode() : 0;
        }
    }

    private class LiteralField extends AbstractFieldSymbol {
        private String typeName;

        LiteralField(int level, String typeName) {
            super(level);
            this.typeName = typeName;
        }

        @Override
        public String getTypeString() {
            return typeName;
        }

        public void setActualArgTypes(Type[] argTypes) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LiteralField that = (LiteralField) o;

            return typeName != null ? typeName.equals(that.typeName) : that.typeName == null;
        }

        @Override
        public int hashCode() {
            return typeName != null ? typeName.hashCode() : 0;
        }
    }

    private class MapField extends AbstractFieldSymbol {
        protected FieldSymbol key;
        protected FieldSymbol value;

        MapField(int level) {
            super(level);
        }

        @Override
        public void setActualArgTypes(Type[] argTypes) {
            if (argTypes.length != 2)
                throw new IllegalArgumentException("arg types count of map must be 2");
            int level = getLevel();
            if (!ProtoBasicTypes.contains(argTypes[0]))
                throw new RuntimeException("Map key must be primitive type, but now is: " + argTypes[0]);
            key = newFieldSymbol(argTypes[0], level + 1);
            value = newFieldSymbol(argTypes[1], level + 1);
        }

        @Override
        public String getTypeString() {
            return "map<" + key.getTypeString() + ", " + value.getTypeString() + ">";
        }

        @Override
        public Set<Message> getInnerMessages() {
            return value.getInnerMessages();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MapField mapField = (MapField) o;

            if (key != null ? !key.equals(mapField.key) : mapField.key != null) return false;
            return value != null ? value.equals(mapField.value) : mapField.value == null;
        }

        @Override
        public int hashCode() {
            int result = key != null ? key.hashCode() : 0;
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }
    }

    private class RepeatedField extends AbstractFieldSymbol {
        private FieldSymbol value;

        RepeatedField(int level) {
            super(level);
        }

        @Override
        public void setActualArgTypes(Type[] argTypes) {
            if (argTypes.length != 1)
                throw new IllegalArgumentException("arg types count of collection must be 1");
            value = newFieldSymbol(argTypes[0], getLevel() + 1);
        }

        @Override
        public String getTypeString() {
            return "repeated " + value.getTypeString();
        }

        @Override
        public Set<Message> getInnerMessages() {
            return value.getInnerMessages();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RepeatedField that = (RepeatedField) o;

            return value != null ? value.equals(that.value) : that.value == null;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }

}
