package org.proto.serdes.code;

import java.util.Arrays;
import java.util.List;

public abstract class CompoundCode<T extends Code, K extends CompoundCode> extends AbstractCode<K> {
    final CodeBody<T> body = new CodeBody<>(this);

    @SuppressWarnings("unchecked")
    public K add(T code) {
        body.add(code);
        return (K) this;
    }

    @Override
    public List<Code> getChildren() {
        return Arrays.asList(body);
    }
}
