package org.proto.utils;

import java.util.Collection;
import java.util.function.Supplier;

public class CheckUtils {
    private static void check(Supplier<Boolean> supplier, String msg) {
        if (supplier.get())
            throw new IllegalArgumentException(msg);
    }

    public static void assertNotEmpty(String s, String msg) {
        check(() -> s == null || s.isEmpty(), msg);
    }

    public static <T> void assertNotEmpty(T[] s, String msg) {
        if (s == null || s.length == 0)
            throw new IllegalArgumentException(msg);
    }

    public static <T extends Collection> void assertNotEmpty(T s, String msg) {
        check(() -> s == null || s.isEmpty(), msg);
    }
}
