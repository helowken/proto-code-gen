package org.proto.serdes.code;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class AbstractCode<T extends Code> implements Code<T> {
    Code parent;
    String endfix = "";

    @Override
    public Code getParent() {
        return parent;
    }

    @Override
    public void setParent(Code parent) {
        this.parent = parent;
    }

    <K extends Code> Optional<K> findAncestorByClass(Class<K> clazz) {
        return findAncestor(code -> code.getClass() == clazz);
    }

    <K extends Code> Optional<K> findAncestor(Predicate<Code> predicate) {
        if (parent != null && predicate.test(parent))
            return Optional.of((K) parent);
        return Optional.empty();
    }

    @Override
    public T end() {
        endfix = ";";
        return (T) this;
    }

    @Override
    public List<Code> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return getContent().toString();
    }
}
