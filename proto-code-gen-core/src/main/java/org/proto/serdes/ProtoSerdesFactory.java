package org.proto.serdes;

import org.proto.serdes.type.TypeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class ProtoSerdesFactory {
    private static final Logger logger = LoggerFactory.getLogger(ProtoSerdesFactory.class);
    private static final ProtoSerdesFactory instance = new ProtoSerdesFactory();
    private static final Map<String, ProtoSerdes> protoClass2Serdes = new HashMap<>();
    private static final Map<Class<?>, ProtoInfoWrapper> class2Wrapper = new HashMap<>();
    private static final Map<String, ProtoInfoWrapper> protoClass2Wrapper = new HashMap<>();

    public static ProtoSerdesFactory getInstance() {
        return instance;
    }

    public static void config(Class<?> pojoClass, Supplier<ProtoInfoWrapper> func) {
        class2Wrapper.computeIfAbsent(pojoClass, clazz -> {
            logger.debug("Create proto info wrapper for class: " + clazz);
            ProtoInfoWrapper wrapper = func.get();
            protoClass2Wrapper.putIfAbsent(wrapper.protoClassName, wrapper);
            return wrapper;
        });
    }

    static boolean hasConfig(Class<?> pojoClass) {
        return class2Wrapper.containsKey(pojoClass);
    }

    static Optional<ProtoInfoWrapper> getConfig(Class<?> pojoClass) {
        return Optional.ofNullable(class2Wrapper.get(pojoClass));
    }

    public <T> ProtoSerdes<T> getSerdes(Class<T> pojoClass) {
        return getSerdes(ProtoUtils.createTypeClass(pojoClass));
    }

    public ProtoInfoWrapper getOrCreateProtoInfoWrapper(Class<?> pojoClass) {
        return getOrCreateProtoInfoWrapper(ProtoUtils.createTypeClass(pojoClass));
    }

    private ProtoInfoWrapper getOrCreateProtoInfoWrapper(TypeClass pojoType) {
        return Optional.ofNullable(class2Wrapper.get(pojoType.getRawClass()))
                .orElseGet(() ->
                        pojoType.getProtoClassName()
                                .map(protoClassName ->
                                        protoClass2Wrapper.computeIfAbsent(
                                                protoClassName,
                                                k -> ProtoUtils.createWrapper(pojoType)
                                        )
                                )
                                .orElseThrow(() ->
                                        new RuntimeException("Pojo class " + pojoType.getRawClass() + " has no proto annotation and no proto info configuration.")
                                )
                );
    }

    public <T> ProtoSerdes<T> getSerdes(final TypeClass pojoType) {
        return getSerdes(getOrCreateProtoInfoWrapper(pojoType));
    }

    private <T> ProtoSerdes<T> getSerdes(final ProtoInfoWrapper wrapper) {
        synchronized (protoClass2Serdes) {
            return protoClass2Serdes.computeIfAbsent(wrapper.protoClassName,
                    k -> {
                        logger.debug("create serdes for pojo type: " + wrapper.getPojoType());
                        try {
                            return wrapper.newSerdes();
                        } catch (Throwable t) {
                            throw ProtoUtils.wrapError(t);
                        }
                    }
            );
        }
    }

}
