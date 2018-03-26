package org.proto.serdes.utils;

public interface ProcessFunc<T> {
    void apply(T v);
}
