package org.proto.serdes.code;

public abstract class CompoundCode<T extends Code, K extends CompoundCode> extends AbstractCode<K> {
    final CodeBody<T> body = new CodeBody<>();

    @SuppressWarnings("unchecked")
    public K add(T code) {
        body.add(code);
        return (K) this;
    }
}
