package org.proto.serdes;

import org.proto.serdes.info.FieldInfo;
import org.proto.serdes.method.GetMethodWrapper;
import org.proto.serdes.method.SetMethodWrapper;

import java.lang.reflect.Field;

class ProtoCodecField extends CodecField {
    final Field pojoField;

    ProtoCodecField(Field pojoField, FieldInfo fieldInfo, SetMethodWrapper pojoSetMethod, GetMethodWrapper pojoGetMethod,
                    SetMethodWrapper protoSetMethod, GetMethodWrapper protoGetMethod, ValueConverter valueConverter) {
        super(fieldInfo, protoSetMethod, protoGetMethod, pojoSetMethod, pojoGetMethod, valueConverter);
        this.pojoField = pojoField;
    }

    @Override
    String getName() {
        return pojoField.getName();
    }

    @Override
    Class<?> getType() {
        return pojoField.getType();
    }


}
