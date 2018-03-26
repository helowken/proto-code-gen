package org.proto.serdes.code;

import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.PrintIntf;
import org.proto.serdes.utils.ProcessFunc;

public interface Code<T extends Code> extends PrintIntf<T> {
    Element getContent();

    default Object getElementContent() {
        return getContent().getElementContent();
    }

    @SuppressWarnings("unchecked")
    default T process(ProcessFunc<T> func) {
        func.apply((T) this);
        return (T) this;
    }

    T end();
}
