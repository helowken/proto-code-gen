package org.proto.serdes.utils;

import java.util.LinkedList;
import java.util.List;

public class AbstractRichString<T extends RichString> implements RichString<T> {
    final String prefix;
    final String suffix;
    protected final List<Object> children = new LinkedList<>();

    public AbstractRichString(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public T add(Object row) {
        children.add(row);
        return (T) this;
    }

    public T add(int index, Object row) {
        children.add(index, row);
        return (T) this;
    }

}
