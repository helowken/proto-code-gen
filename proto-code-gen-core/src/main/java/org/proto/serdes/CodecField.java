package org.proto.serdes;

import org.proto.serdes.info.FieldInfo;
import org.proto.serdes.method.GetMethodWrapper;
import org.proto.serdes.method.SetMethodWrapper;

abstract class CodecField {
    final FieldInfo fieldInfo;
    final SetMethodWrapper protoSetMethod;
    final GetMethodWrapper protoGetMethod;
    final SetMethodWrapper pojoSetMethod;
    final GetMethodWrapper pojoGetMethod;
    final ValueConverter valueConverter;

    CodecField(FieldInfo fieldInfo, SetMethodWrapper protoSetMethod, GetMethodWrapper protoGetMethod,
               SetMethodWrapper pojoSetMethod, GetMethodWrapper pojoGetMethod, ValueConverter valueConverter) {
        this.fieldInfo = fieldInfo;
        this.protoSetMethod = protoSetMethod;
        this.protoGetMethod = protoGetMethod;
        this.pojoSetMethod = pojoSetMethod;
        this.pojoGetMethod = pojoGetMethod;
        this.valueConverter = valueConverter;
    }

    abstract String getName();

    abstract Class<?> getType();
}
