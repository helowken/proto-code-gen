package org.proto.serdes;

import org.proto.serdes.type.TypeClass;

public class DefaultValueConverter implements ValueConverter {
    @SuppressWarnings("unchecked")
    @Override
    public Object apply(CodecField codecField, Object oldValue, ConvertFunc convertFunc) throws Exception {
        TypeClass fieldTypeClass = codecField.fieldInfo.getTypeClass();
        if (fieldTypeClass.isProto())
            return convertFunc.apply(ProtoSerdesFactory.getInstance().getSerdes(fieldTypeClass), oldValue);
        return oldValue;
    }
}
