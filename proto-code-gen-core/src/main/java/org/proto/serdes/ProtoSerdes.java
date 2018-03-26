package org.proto.serdes;

import com.google.protobuf.MessageLite;

import java.util.List;

public interface ProtoSerdes<T> {
    List<? extends CodecField> getCodecFields();

    MessageLite toProto(Object pojo);

    T toPojo(Object protoObj);

    byte[] serialize(T pojo);

    T deserialize(byte[] bs);
}
