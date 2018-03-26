package org.proto.serdes;

import org.proto.serdes.type.TypeClass;
import org.proto.serdes.utils.Pair;
import org.proto.serdes.info.FieldInfo;
import com.google.protobuf.MessageLite;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class ProtoInfoWrapper {
    public final String protoClassName;
    private final TypeClass pojoType;
    private Class<? extends MessageLite> protoClass;
    private List<Pair<Field, FieldInfo>> fieldPairs;
    private final Supplier<List<Pair<Field, FieldInfo>>> func;

    ProtoInfoWrapper(TypeClass pojoType, String protoClassName, Supplier<List<Pair<Field, FieldInfo>>> func) throws ClassNotFoundException {
        this.pojoType = pojoType;
        this.protoClassName = protoClassName;
        this.func = func;
    }

    public TypeClass getPojoType() {
        return pojoType;
    }

    public Class<? extends MessageLite> getProtoClass() {
        if (protoClass == null)
            protoClass = ProtoUtils.newProtoClass(protoClassName);
        return protoClass;
    }

    public List<Pair<Field, FieldInfo>> getFieldPairs() {
        if (fieldPairs == null)
            fieldPairs = Collections.unmodifiableList(func.get());
        return fieldPairs;
    }

    public ProtoSerdes<?> newSerdes() throws Throwable {
        if (pojoType.isInner())
            return InnerProtoSerdes.create(this);
        return ProtoSerdesImpl.create(this);
    }
}
