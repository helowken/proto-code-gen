package org.proto.serdes;

import org.proto.serdes.transform.TransformMgr;
import org.proto.serdes.type.TypeClass;

public class DefaultValueConverter implements ValueConverter {
    @SuppressWarnings("unchecked")
    @Override
    public Object apply(TypeClass valueType, Object oldValue, ConvertFunc convertFunc, TransformMgr.FuncCache funcCache) {
        if (valueType.isProto())
            return convertFunc.apply(ProtoSerdesFactory.getInstance().getSerdes(valueType), oldValue);
        return funcCache.getValue(valueType.getRawClass(), oldValue);
    }
}
