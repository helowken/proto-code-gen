package org.proto.serdes.transform;

import org.proto.serdes.code.TypeCode;
import org.proto.serdes.code.CastCode;
import org.proto.serdes.code.Code;
import org.proto.serdes.code.MethodCallCode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TransformMgr {
    public static final FuncCache toPojoCache = new FuncCache();
    public static final FuncCache toProtoCache = new FuncCache();
    private static final TransformFunc defaultPrimitiveToProto = new PrimitiveToProtoFunc();
    private static final TransformFunc defaultBoxToProto = new BoxToProtoFunc();

    static {
        reg(byte.class, new PrimitiveByteToPojoFunc(), defaultPrimitiveToProto);
        reg(Byte.class, new BoxByteToPojoFunc(), defaultBoxToProto);

        reg(short.class, new PrimitiveShortToPojoFunc(), defaultPrimitiveToProto);
        reg(Short.class, new BoxShortToPojoFunc(), defaultBoxToProto);

        reg(char.class, new PrimitiveCharToPojoFunc(), new PrimitiveCharToProtoFunc());
        reg(Character.class, new BoxCharToPojoFunc(), new BoxCharToProtoFunc());
    }

    public static synchronized void reg(Class<?> clazz, TransformFunc toPojoFunc, TransformFunc toProtoFunc) {
        toPojoCache.funcMap.put(clazz, toPojoFunc);
        toProtoCache.funcMap.put(clazz, toProtoFunc);
    }

    public static synchronized void unreg(Class<?> clazz) {
        toPojoCache.funcMap.remove(clazz);
        toProtoCache.funcMap.remove(clazz);
    }

    public static class FuncCache {
        private final Map<Class<?>, TransformFunc> funcMap = new HashMap<>();

        public Optional<TransformFunc> get(Class<?> clazz) {
            return Optional.ofNullable(funcMap.get(clazz));
        }
    }

    private static MethodCallCode newNumberMethodCallCode(Code inputCode, String valueMethod, boolean needToBox) {
        return new MethodCallCode(
                needToBox ?
                        new CastCode(new TypeCode("Number"), inputCode).wrap() :
                        inputCode,
                valueMethod
        );
    }

    private static class PrimitiveToProtoFunc implements TransformFunc {
        @Override
        public Object transform(Object oldValue) {
            return ((Number) oldValue).intValue();
        }

        @Override
        public Code genCode(Code inputCode, boolean boxed) {
            return inputCode;
        }
    }

    private static class PrimitiveCharToProtoFunc extends PrimitiveToProtoFunc {
        @Override
        public Object transform(Object oldValue) {
            return (int) (char) oldValue;
        }
    }

    private static abstract class PrimitiveToPojoFunc implements TransformFunc {
        private final String typeName;

        PrimitiveToPojoFunc(String typeName) {
            this.typeName = typeName;
        }

        @Override
        public Code genCode(Code inputCode, boolean boxed) {
            return new CastCode(new TypeCode(typeName), inputCode);
        }
    }

    private static abstract class BoxToPojoFunc implements TransformFunc {
        private final String methodName;

        BoxToPojoFunc(String methodName) {
            this.methodName = methodName;
        }

        @Override
        public Code genCode(Code inputCode, boolean boxed) {
            return newNumberMethodCallCode(inputCode, methodName, !boxed);
        }
    }

    private static class PrimitiveShortToPojoFunc extends PrimitiveToPojoFunc {
        PrimitiveShortToPojoFunc() {
            super("short");
        }

        @Override
        public Object transform(Object oldValue) {
            return ((Number) oldValue).shortValue();
        }
    }

    private static class PrimitiveByteToPojoFunc extends PrimitiveToPojoFunc {
        PrimitiveByteToPojoFunc() {
            super("byte");
        }

        @Override
        public Object transform(Object oldValue) {
            return ((Number) oldValue).byteValue();
        }
    }

    private static class PrimitiveCharToPojoFunc implements TransformFunc {
        @Override
        public Object transform(Object oldValue) {
            return (char) ((Number) oldValue).byteValue();
        }

        @Override
        public Code genCode(Code inputCode, boolean boxed) {
            return new CastCode(
                    new TypeCode("char"),
                    newNumberMethodCallCode(inputCode, "byteValue", !boxed)
            );
        }
    }

    private static class BoxShortToPojoFunc extends BoxToPojoFunc {
        BoxShortToPojoFunc() {
            super("shortValue");
        }

        @Override
        public Object transform(Object oldValue) {
            return ((Number) oldValue).shortValue();
        }
    }

    private static class BoxByteToPojoFunc extends BoxToPojoFunc {
        BoxByteToPojoFunc() {
            super("byteValue");
        }

        @Override
        public Object transform(Object oldValue) {
            return ((Number) oldValue).byteValue();
        }
    }

    private static class BoxCharToPojoFunc extends BoxByteToPojoFunc {
        @Override
        public Object transform(Object oldValue) {
            return (char) (int) oldValue;
        }

        @Override
        public Code genCode(Code inputCode, boolean boxed) {
            return new CastCode(
                    new TypeCode("char"),
                    super.genCode(inputCode, boxed)
            );
        }
    }

    private static class BoxToProtoFunc implements TransformFunc {
        @Override
        public Object transform(Object oldValue) {
            return ((Number) oldValue).intValue();
        }

        @Override
        public Code genCode(Code inputCode, boolean boxed) {
            return newNumberMethodCallCode(inputCode, "intValue", false);
        }
    }

    private static class BoxCharToProtoFunc implements TransformFunc {
        @Override
        public Object transform(Object oldValue) {
            return (int) (char) oldValue;
        }

        @Override
        public Code genCode(Code inputCode, boolean boxed) {
            return boxed ?
                    new CastCode(new TypeCode("int"), inputCode) :
                    inputCode;
        }
    }
}
