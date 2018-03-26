package org.proto.serdes.utils;

public interface RichString<T extends RichString> extends PrintIntf<T> {
    @SuppressWarnings("unchecked")
    default T process(ProcessFunc<T> func) {
        func.apply((T) this);
        return (T) this;
    }
}
