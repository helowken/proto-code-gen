package org.proto.serdes.code;

public abstract class AbstractCode<T extends Code> implements Code<T> {
    String endfix = "";

    @Override
    public T end() {
        endfix = ";";
        return (T) this;
    }

    @Override
    public String toString() {
        return getContent().toString();
    }
}
