package org.proto.serdes;

interface ConvertFunc<T, K> {
    K apply(ProtoSerdes<?> serdes, T value);
}
