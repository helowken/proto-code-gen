package org.proto.serdes;

import org.proto.serdes.info.FieldInfo;
import org.proto.serdes.method.GetMethodWrapper;
import org.proto.serdes.method.SetMethodWrapper;

class InnerCodecField extends CodecField {

    InnerCodecField(FieldInfo fieldInfo, SetMethodWrapper protoSetMethod, GetMethodWrapper protoGetMethod,
                    SetMethodWrapper pojoSetMethod, ValueConverter valueConverter) {
        super(fieldInfo, protoSetMethod, protoGetMethod, pojoSetMethod, null, valueConverter);
    }

    @Override
    String getName() {
        return ProtoConst.INNER_DEFAULT_FIELD_NAME;
    }

    @Override
    Class<?> getType() {
        return fieldInfo.getTypeClass().getRawClass();
    }
}
