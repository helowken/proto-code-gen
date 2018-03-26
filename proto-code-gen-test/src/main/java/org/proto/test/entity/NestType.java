package org.proto.test.entity;

import org.proto.serdes.annotation.ProtoClass;
import org.proto.serdes.annotation.ProtoField;

import java.util.List;
import java.util.Map;

@ProtoClass(protoClass = "ProtoNestType")
public class NestType {
    @ProtoField(index = 1)
    private Map<String, Integer> a1;
    @ProtoField(index = 2)
    private Map<String, Map<String, Integer>> a2;
    @ProtoField(index = 3)
    private Map<String, List<Integer>> a3;
    @ProtoField(index = 4)
    private Map<String, Map<String, List<Integer>>> a4;
    @ProtoField(index = 5)
    private Map<String, Map<String, Map<String, Integer>>> a5;

    @ProtoField(index = 6)
    private List<Integer> a6;
    @ProtoField(index = 7)
    private List<Map<String, Integer>> a7;
    @ProtoField(index = 8)
    private List<List<Integer>> a8;
    @ProtoField(index = 9)
    private List<List<List<Integer>>> a9;
    @ProtoField(index = 10)
    private List<List<Map<String, Integer>>> a10;

    @ProtoField(index = 11)
    public Map<String, Integer> a11;
    @ProtoField(index = 12)
    public Map<String, Map<String, Integer>> a12;
    @ProtoField(index = 13)
    public Map<String, List<Integer>> a13;
    @ProtoField(index = 14)
    public Map<String, Map<String, List<Integer>>> a14;
    @ProtoField(index = 15)
    public Map<String, Map<String, Map<String, Integer>>> a15;

    @ProtoField(index = 16)
    public List<Integer> a16;
    @ProtoField(index = 17)
    public List<Map<String, Integer>> a17;
    @ProtoField(index = 18)
    public List<List<Integer>> a18;
    @ProtoField(index = 19)
    public List<List<List<Integer>>> a19;
    @ProtoField(index = 20)
    public List<List<Map<String, Integer>>> a20;

    public Map<String, Integer> getA1() {
        return a1;
    }

    public void setA1(Map<String, Integer> a1) {
        this.a1 = a1;
    }

    public Map<String, Map<String, Integer>> getA2() {
        return a2;
    }

    public void setA2(Map<String, Map<String, Integer>> a2) {
        this.a2 = a2;
    }

    public Map<String, List<Integer>> getA3() {
        return a3;
    }

    public void setA3(Map<String, List<Integer>> a3) {
        this.a3 = a3;
    }

    public Map<String, Map<String, List<Integer>>> getA4() {
        return a4;
    }

    public void setA4(Map<String, Map<String, List<Integer>>> a4) {
        this.a4 = a4;
    }

    public Map<String, Map<String, Map<String, Integer>>> getA5() {
        return a5;
    }

    public void setA5(Map<String, Map<String, Map<String, Integer>>> a5) {
        this.a5 = a5;
    }

    public List<Integer> getA6() {
        return a6;
    }

    public void setA6(List<Integer> a6) {
        this.a6 = a6;
    }

    public List<Map<String, Integer>> getA7() {
        return a7;
    }

    public void setA7(List<Map<String, Integer>> a7) {
        this.a7 = a7;
    }

    public List<List<Integer>> getA8() {
        return a8;
    }

    public void setA8(List<List<Integer>> a8) {
        this.a8 = a8;
    }

    public List<List<List<Integer>>> getA9() {
        return a9;
    }

    public void setA9(List<List<List<Integer>>> a9) {
        this.a9 = a9;
    }

    public List<List<Map<String, Integer>>> getA10() {
        return a10;
    }

    public void setA10(List<List<Map<String, Integer>>> a10) {
        this.a10 = a10;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NestType)) return false;

        NestType nestType = (NestType) o;

        if (a1 != null ? !a1.equals(nestType.a1) : nestType.a1 != null) return false;
        if (a2 != null ? !a2.equals(nestType.a2) : nestType.a2 != null) return false;
        if (a3 != null ? !a3.equals(nestType.a3) : nestType.a3 != null) return false;
        if (a4 != null ? !a4.equals(nestType.a4) : nestType.a4 != null) return false;
        if (a5 != null ? !a5.equals(nestType.a5) : nestType.a5 != null) return false;
        if (a6 != null ? !a6.equals(nestType.a6) : nestType.a6 != null) return false;
        if (a7 != null ? !a7.equals(nestType.a7) : nestType.a7 != null) return false;
        if (a8 != null ? !a8.equals(nestType.a8) : nestType.a8 != null) return false;
        if (a9 != null ? !a9.equals(nestType.a9) : nestType.a9 != null) return false;
        return a10 != null ? a10.equals(nestType.a10) : nestType.a10 == null;
    }

    @Override
    public int hashCode() {
        int result = a1 != null ? a1.hashCode() : 0;
        result = 31 * result + (a2 != null ? a2.hashCode() : 0);
        result = 31 * result + (a3 != null ? a3.hashCode() : 0);
        result = 31 * result + (a4 != null ? a4.hashCode() : 0);
        result = 31 * result + (a5 != null ? a5.hashCode() : 0);
        result = 31 * result + (a6 != null ? a6.hashCode() : 0);
        result = 31 * result + (a7 != null ? a7.hashCode() : 0);
        result = 31 * result + (a8 != null ? a8.hashCode() : 0);
        result = 31 * result + (a9 != null ? a9.hashCode() : 0);
        result = 31 * result + (a10 != null ? a10.hashCode() : 0);
        return result;
    }
}
