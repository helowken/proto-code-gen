package org.proto.serdes.utils;

public class Pair<T, V> {
    public final T left;
    public final V right;

    public Pair(T left, V right) {
        this.left = left;
        this.right = right;
    }
}
