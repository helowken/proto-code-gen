package org.proto.serdes.utils;

import org.proto.serdes.code.Code;

public interface Element<T extends Element> extends PrintIntf<T> {
    Object getElementContent();

    default Object convert(Object v) {
        if (v instanceof Code)
            return ((Code) v).getElementContent();
        if (v instanceof Element)
            return ((Element) v).getElementContent();
        return v;
    }

    T add(Object v);

    @SuppressWarnings("unchecked")
    default T process(ProcessFunc<T> func) {
        func.apply((T) this);
        return (T) this;
    }
}
