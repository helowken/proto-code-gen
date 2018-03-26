package org.proto.serdes.utils;

public interface PrintIntf<T> {
    @SuppressWarnings("unchecked")
    default T print() {
        System.out.println(this);
        return (T) this;
    }
}
