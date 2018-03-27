package org.proto.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

class ProtoCTUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    static void printJson(Object o) throws Throwable {
        String s = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        System.out.println(s);
    }

    static void printLine() {
        System.out.println("=======================");
    }

    static List<Integer> newList() {
        return Arrays.asList(1, 2, 3);
    }

    static Map<String, Short> newShortMap() {
        Map<String, Short> map = new HashMap<>();
        for (int i = 0; i < 3; ++i) {
            map.put("k" + i, (short) i);
        }
        return map;
    }

    static Map<String, Byte> newByteMap() {
        Map<String, Byte> map = new HashMap<>();
        for (int i = 0; i < 3; ++i) {
            map.put("k" + i, (byte) i);
        }
        return map;
    }

    static Map<String, Character> newCharMap() {
        Map<String, Character> map = new HashMap<>();
        for (int i = 0; i < 3; ++i) {
            map.put("k" + i, (char) i);
        }
        return map;
    }

    static Map<String, Integer> newMap() {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < 3; ++i) {
            map.put("k" + i, i);
        }
        return map;
    }

    static <T> Map<String, T> newMap(OP<T> op) {
        Map<String, T> map = new HashMap<>();
        for (int i = 0; i < 3; ++i) {
            map.put("k" + i, op.apply());
        }
        return map;
    }

    static <T> List<T> newList(OP<T> op) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            list.add(op.apply());
        }
        return list;
    }

    interface OP<T> {
        T apply();
    }
}
