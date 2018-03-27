package org.proto.serdes;

import com.google.protobuf.InvalidProtocolBufferException;
import org.proto.serdes.code.*;
import org.proto.serdes.transform.TransformMgr;
import org.proto.serdes.type.*;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class ProtoCodeGen {
    private static final String CONVERT_TO = "convertTo";
    private static final String CONVERT_TO_PROTO = CONVERT_TO + "Proto";
    private static final String CONVERT_TO_POJO = CONVERT_TO + "Pojo";
    private static final String SERIALIZE = "serialize";
    private static final String DESERIALIZE = "deserialize";
    private static final String OLD_VALUE = "oldValue";
    private static final String NEW_VALUE = "newValue";
    private static final String TMP_VALUE = "v";

    private static final Map<Class<? extends TypeClass>, ValueGen> protoValueGenMap = new HashMap<>();
    private static final Map<Class<? extends TypeClass>, ValueGen> pojoValueGenMap = new HashMap<>();
    private static final Map<CodecField, String> codecField2ClassTag = new HashMap<>();

    private static final Function<CodecField, Code> defaultProtoValueGen = codecField ->
            Optional.ofNullable(codecField.pojoGetMethod)
                    .map(method -> method.genCode(new VariableCode(OLD_VALUE)))
                    .orElse(new VariableCode(OLD_VALUE));

    private static final Function<CodecField, Code> defaultPojoValueGen = codecField ->
            codecField.protoGetMethod.genCode(new VariableCode(OLD_VALUE));

    static {
        protoValueGenMap.put(SetFieldClass.class, ProtoCodeGen::newProtoCollection);
        protoValueGenMap.put(ListFieldClass.class, ProtoCodeGen::newProtoCollection);
        protoValueGenMap.put(MapFieldClass.class, ProtoCodeGen::newProtoMap);

        pojoValueGenMap.put(SetFieldClass.class, ProtoCodeGen::newPojoCollection);
        pojoValueGenMap.put(ListFieldClass.class, ProtoCodeGen::newPojoCollection);
        pojoValueGenMap.put(MapFieldClass.class, ProtoCodeGen::newPojoMap);
    }

    private static Code newStruct(CodecField codecField, ClassCode classCode,
                                  Code inputCode,
                                  String methodPrefix, StructGenFunc structGenFunc,
                                  Function<TypeClass, Optional<Function<Code, Code>>> valueConverterCreator,
                                  Function<TypeClass, AssignmentCode<ParameterCode>> newCodeSupplier,
                                  Function<TypeClass, TypeCode> inputParamTypeFunc) {
        CompoundTypeClass typeClass = (CompoundTypeClass) codecField.fieldInfo.getTypeClass();
//        addImport(typeClass, classCode);
        TypeClass valueType = typeClass.getValueType();
        return valueConverterCreator.apply(valueType)
                .<Code>map(valueConverter -> {
                    String methodName = ProtoUtils.getBeanMethodName(methodPrefix, codecField.getName());
                    classCode.addMethod(methodName, () -> {
                        AssignmentCode<ParameterCode> newCode = newCodeSupplier.apply(typeClass);
                        TypeCode inputTypeCode = inputParamTypeFunc.apply(typeClass);
                        return new MethodDefineCode(
                                Modifier.STATIC | Modifier.PRIVATE,
                                newCode.getLeft().getType(),
                                methodName,
                                new ParameterCode(inputTypeCode, OLD_VALUE)
                        )
                                .add(newCode)
                                .process(mc -> structGenFunc.apply(mc, typeClass, valueType, inputTypeCode, valueConverter))
                                .add(new ReturnCode(NEW_VALUE).end());
                    });
                    return new MethodCallCode(null, methodName, inputCode);
                })
                .orElseGet(() ->
                        new NewCode(typeClass.getInstanceJavaCode(), inputCode)
                );
    }

    private static Function<TypeClass, Optional<Function<Code, Code>>> createValueConverterCreator(ClassCode classCode,
                                                                                                   ConvertFuncGen convertFuncGen, TransformMgr.FuncCache funcCache) {
        return valueType -> valueType.isProto() ?
                Optional.of(valueCode ->
                        new MethodCallCode(null,
                                convertFuncGen.apply(valueType, classCode).getName(),
                                valueCode
                        )
                ) :
                funcCache.get(valueType.getRawClass())
                        .map(transformFunc ->
                                valueCode -> transformFunc.genCode(valueCode, true)
                        );
    }

    private static Code newProtoStruct(CodecField codecField, ClassCode classCode, Code inputCode, StructGenFunc func) {
        return newStruct(codecField, classCode, inputCode, getFieldMethodPrefix(codecField), func,
                createValueConverterCreator(classCode, ProtoCodeGen::genToProto, TransformMgr.toProtoCache),
                typeClass -> createNew(typeClass.getGenericProtoCode(), typeClass.getInstanceProtoCode()),
                TypeClass::getGenericJavaCode
        );
    }

    private static Code newPojoStruct(CodecField codecField, ClassCode classCode, Code inputCode, StructGenFunc func) {
        return newStruct(codecField, classCode, inputCode, getFieldMethodPrefix(codecField), func,
                createValueConverterCreator(classCode, ProtoCodeGen::genToPojo, TransformMgr.toPojoCache),
                typeClass -> createNew(typeClass.getGenericJavaCode(), typeClass.getInstanceJavaCode()),
                TypeClass::getGenericProtoCode
        );
    }

    private static Code newCollection(CodecField codecField, ClassCode classCode, Code inputCode, StructGenCreator structGenCreator) {
        return structGenCreator.apply(codecField, classCode, inputCode,
                (body, typeClass, valueType, inputTypeCode, valueConverter) ->
                        body.add(
                                new ForStruct(
                                        new ParameterCode(inputTypeCode.getArgs().get(0), TMP_VALUE),
                                        new VariableCode(OLD_VALUE)
                                ).add(MethodCallCode.create(NEW_VALUE,
                                        "add",
                                        valueConverter.apply(new VariableCode(TMP_VALUE))
                                        ).end()
                                )
                        )
        );
    }

    private static Code newProtoCollection(CodecField codecField, ClassCode classCode, Code inputCode) {
        return newCollection(codecField, classCode, inputCode, ProtoCodeGen::newProtoStruct);
    }

    private static Code newPojoCollection(CodecField codecField, ClassCode classCode, Code inputCode) {
        return newCollection(codecField, classCode, inputCode, ProtoCodeGen::newPojoStruct);
    }

    private static Code newMap(CodecField codecField, ClassCode classCode, Code inputCode, StructGenCreator structGenCreator) {
        return structGenCreator.apply(codecField, classCode, inputCode,
                (body, typeClass, valueType, inputTypeCode, valueConverter) -> {
                    String varName = "entry";
                    body.add(
                            new ForStruct.ForMapEntry(inputTypeCode.getArgs().get(0),
                                    inputTypeCode.getArgs().get(1),
                                    varName,
                                    OLD_VALUE
                            ).add(
                                    MethodCallCode.create(NEW_VALUE,
                                            "put",
                                            MethodCallCode.create(varName, "getKey"),
                                            valueConverter.apply(
                                                    MethodCallCode.create(varName, "getValue")
                                            )
                                    ).end()
                            )
                    );
                }
        );
    }

    private static Code newProtoMap(CodecField codecField, ClassCode classCode, Code inputCode) {
        return newMap(codecField, classCode, inputCode, ProtoCodeGen::newProtoStruct);
    }

    private static Code newPojoMap(CodecField codecField, ClassCode classCode, Code inputCode) {
        return newMap(codecField, classCode, inputCode, ProtoCodeGen::newPojoStruct);
    }

    private static int getModifier(TypeClass pojoType) {
        return Modifier.STATIC | (pojoType.isInner() ? Modifier.PRIVATE : Modifier.PUBLIC);
    }

    private static String getSimpleName(TypeClass pojoType) {
        return pojoType.getRawClass().getSimpleName();
    }

    private static String getShortName(TypeClass pojoType, String methodName, String shortName) {
        return pojoType.isInner() || getSimpleName(pojoType).equals(pojoType.getProtoClassNameOrError()) ? methodName : shortName;
    }

    private static String getFieldMethodPrefix(CodecField codecField) {
        return Optional.ofNullable(codecField2ClassTag.get(codecField))
                .orElseThrow(() -> new RuntimeException("No class tag found by codec field: " + codecField));
    }

    private static MethodDefineCode genToPojo(TypeClass pojoType, ClassCode classCode) {
        String methodName = CONVERT_TO_POJO + (pojoType.isInner() ? pojoType.getProtoClassNameOrError() : getSimpleName(pojoType));
        String shortName = getShortName(pojoType, methodName, CONVERT_TO);
        ProtoSerdes<?> serdes = ProtoSerdesFactory.getInstance().getSerdes(pojoType);
//        addImport(pojoType, classCode);
        return classCode.addMethod(methodName, () ->
                new MethodDefineCode(
                        getModifier(pojoType),
                        pojoType.getGenericJavaCode(),
                        shortName,
                        new ParameterCode(pojoType.getProtoCode(), OLD_VALUE)
                )
                        .add(createNew(pojoType, classCode))
                        .process(mc ->
                                serdes.getCodecFields().forEach(codecField -> {
                                    codecField2ClassTag.put(codecField, methodName);
                                    mc.add(
                                            codecField.pojoSetMethod.genCode(
                                                    new VariableCode(NEW_VALUE),
                                                    genPojoValue(codecField, classCode, defaultPojoValueGen.apply(codecField))
                                            ).end()
                                    );
                                })
                        )
                        .add(new ReturnCode(NEW_VALUE).end())
                        .process(mc -> {
                            if (pojoType.isProto() && !pojoType.isInner())
                                genDeserialize(mc, pojoType, classCode);
                        })
        );
    }

    private static MethodDefineCode genToProto(TypeClass pojoType, ClassCode classCode) {
        try {
//            addImport(pojoType, classCode);
            ProtoSerdes<?> serdes = ProtoSerdesFactory.getInstance().getSerdes(pojoType);
            String protoClass = pojoType.getProtoClassNameOrError();
            String builderClassName = ProtoConst.DEFAULT_JAVA_PACKAGE + "." + protoClass + "$" + ProtoConst.BUILDER_CLASS;
            Class<?> builderClass = ProtoUtils.loadClass(builderClassName);
//            classCode.addImport(ProtoUtils.loadClass(ProtoConst.DEFAULT_JAVA_PACKAGE + "." + protoClass));
            String methodName = CONVERT_TO_PROTO + protoClass;
            String shortName = getShortName(pojoType, methodName, CONVERT_TO);
            TypeCode typeCode = pojoType.getProtoCode();
            AtomicInteger fieldCount = new AtomicInteger(0);
            return classCode.addMethod(methodName, () ->
                            new MethodDefineCode(
                                    getModifier(pojoType),
                                    typeCode,
                                    shortName,
                                    new ParameterCode(pojoType.getGenericJavaCode(), OLD_VALUE)
                            )
                                    .add(new AssignmentCode<>(
                                                    new ParameterCode(new TypeCode(builderClass), NEW_VALUE),
                                                    new MethodCallCode(typeCode, ProtoConst.NEW_BUILDER_METHOD)
                                            ).end()
                                    )
                                    .process(mc ->
                                                    serdes.getCodecFields().forEach(codecField -> {
                                                        codecField2ClassTag.put(codecField, methodName);
                                                        Code inputCode = defaultProtoValueGen.apply(codecField);
                                                        TypeClass fieldTypeClass = codecField.fieldInfo.getTypeClass();
                                                        Function<Code, Code> func = code ->
                                                                codecField.protoSetMethod.genCode(
                                                                        new VariableCode(NEW_VALUE),
                                                                        genProtoValue(codecField, classCode, code)
                                                                ).end();
                                                        if (fieldTypeClass.getRawClass().isPrimitive()) {
                                                            mc.add(func.apply(inputCode));
                                                        } else {
                                                            if (!(inputCode instanceof VariableCode)) {
//                                                addImport(fieldTypeClass, classCode);
                                                                VariableCode valueCode = new VariableCode(TMP_VALUE + fieldCount.addAndGet(1));
                                                                mc.add(
                                                                        new AssignmentCode<>(
                                                                                new ParameterCode(
                                                                                        fieldTypeClass.getGenericJavaCode(),
                                                                                        valueCode.getName()
                                                                                ),
                                                                                inputCode
                                                                        ).end()
                                                                );
                                                                inputCode = valueCode;
                                                            }
                                                            mc.add(
                                                                    new IfStruct(
                                                                            ExprCode.isNotNull(inputCode)
                                                                    ).add(func.apply(inputCode))
                                                            );
                                                        }
                                                    })
                                    )
                                    .add(new ReturnCode(
                                                    MethodCallCode.create(NEW_VALUE, ProtoConst.BUILD_METHOD)
                                            ).end()
                                    )
                                    .process(mc -> {
                                        if (pojoType.isProto() && !pojoType.isInner())
                                            genSerialize(mc, pojoType, classCode);
                                    })
            );
        } catch (Exception e) {
            throw ProtoUtils.wrapError(e);
        }
    }

    private static void genSerialize(MethodDefineCode convertMethodCode, TypeClass pojoType, ClassCode classCode) {
        String methodName = SERIALIZE + getSimpleName(pojoType);
        String shortName = getShortName(pojoType, methodName, SERIALIZE);
        classCode.addMethod(methodName, () ->
                new MethodDefineCode(
                        getModifier(pojoType),
                        new TypeCode(byte[].class),
                        shortName,
                        new ParameterCode(pojoType.getGenericJavaCode(), OLD_VALUE)
                ).add(new CodeChain(false)
                        .add(new ReturnCode(
                                        MethodCallCode.create(null,
                                                convertMethodCode.getName(),
                                                OLD_VALUE
                                        )
                                )
                        ).add(MethodCallCode.create("", ProtoConst.TO_BYTE_ARRAY_METHOD)
                                .end()
                        )
                )
        );
    }

    private static void genDeserialize(MethodDefineCode convertMethodCode, TypeClass pojoType, ClassCode classCode) {
        String methodName = DESERIALIZE + getSimpleName(pojoType);
        Class<?> errClass = InvalidProtocolBufferException.class;
        classCode.addImport(errClass);
        classCode.addMethod(methodName, () ->
                new MethodDefineCode(
                        getModifier(pojoType),
                        pojoType.getGenericJavaCode(),
                        methodName,
                        new ParameterCode(byte[].class, OLD_VALUE)
                )
                        .addThrowable(errClass)
                        .add(new ReturnCode(
                                        MethodCallCode.create(null,
                                                convertMethodCode.getName(),
                                                MethodCallCode.create(
                                                        pojoType.getProtoClassNameOrError(),
                                                        ProtoConst.PARSE_FROM_METHOD,
                                                        OLD_VALUE
                                                )
                                        )
                                ).end()
                        )
        );
    }

//    private static void addImport(TypeClass typeClass, ClassCode classCode) {
//        classCode.addImport(typeClass.getRawClass());
//        classCode.addImport(typeClass.getInstanceClass());
//    }

    private static Code createNew(TypeClass typeClass, ClassCode classCode) {
//        addImport(typeClass, classCode);
        return createNew(typeClass.getGenericJavaCode(), typeClass.getInstanceJavaCode());
    }

    private static AssignmentCode<ParameterCode> createNew(TypeCode srcType, TypeCode targetType) {
        return new AssignmentCode<>(
                new ParameterCode(srcType, NEW_VALUE),
                new NewCode(targetType)
        ).end();
    }

    private static Code genProtoValue(CodecField codecField, ClassCode classCode, Code inputCode) {
        return genValue(protoValueGenMap, codecField, classCode, inputCode, ProtoCodeGen::genToProto, TransformMgr.toProtoCache);
    }

    private static Code genPojoValue(CodecField codecField, ClassCode classCode, Code inputCode) {
        return genValue(pojoValueGenMap, codecField, classCode, inputCode, ProtoCodeGen::genToPojo, TransformMgr.toPojoCache);
    }

    private static Code genValue(Map<Class<? extends TypeClass>, ValueGen> genMap, CodecField codecField, ClassCode classCode,
                                 Code inputCode, ConvertFuncGen convertFuncGen, TransformMgr.FuncCache funcCache) {
        TypeClass fieldTypeClass = codecField.fieldInfo.getTypeClass();
        final Code rsCode = Optional.ofNullable(genMap.get(fieldTypeClass.getClass()))
                .map(valueGen -> valueGen.apply(codecField, classCode, inputCode))
                .orElseGet(() ->
                        fieldTypeClass.isProto() ?
                                MethodCallCode.create(null,
                                        convertFuncGen.apply(fieldTypeClass, classCode).getName(),
                                        inputCode
                                ) :
                                inputCode
                );
        return funcCache.get(codecField.getType())
                .map(transformFunc ->
                        transformFunc.genCode(rsCode, false)
                )
                .orElse(rsCode);
    }

    interface ConvertFuncGen {
        MethodDefineCode apply(TypeClass typeClass, ClassCode classCode);
    }

    interface ValueGen {
        Code apply(CodecField codecField, ClassCode classCode, Code inputCode);
    }

    interface StructGenCreator {
        Code apply(CodecField codecField, ClassCode classCode, Code inputCode, StructGenFunc func);
    }

    interface StructGenFunc {
        void apply(CompoundCode<Code, MethodDefineCode> code, TypeClass typeClass, TypeClass valueType,
                   TypeCode inputTypeCode, Function<Code, Code> valueConverter);
    }

    public static ClassCode generate(String packageName, String className, Class<?>... classes) {
        if (classes == null)
            throw new IllegalArgumentException("Classes can not be empty.");
        ClassCode classCode = new ClassCode(packageName, Modifier.PUBLIC, className);
        for (Class<?> clazz : classes) {
            TypeClass typeClass = ProtoUtils.createTypeClass(clazz);
            genToProto(typeClass, classCode);
            genToPojo(typeClass, classCode);
        }
        return classCode;
    }

}
