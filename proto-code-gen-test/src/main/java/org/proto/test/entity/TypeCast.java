package org.proto.test.entity;

import org.proto.serdes.annotation.ProtoClass;
import org.proto.serdes.annotation.ProtoField;

import java.util.List;
import java.util.Map;
import java.util.Set;

@ProtoClass(protoClass = "ProtoTypeCast")
public class TypeCast {
    @ProtoField(index = 1)
    public short a;
    @ProtoField(index = 2)
    public Short a2;
    @ProtoField(index = 3)
    public Map<String, Short> a3;
    @ProtoField(index = 4)
    public Set<Short> a4;
    @ProtoField(index = 5)
    public List<Short> a5;

    @ProtoField(index = 6)
    public char c;
    @ProtoField(index = 7)
    public Character c2;
    @ProtoField(index = 8)
    public Map<String, Character> c3;
    @ProtoField(index = 9)
    public Set<Character> c4;
    @ProtoField(index = 10)
    public List<Character> c5;

    @ProtoField(index = 11)
    public byte b;
    @ProtoField(index = 12)
    public Byte b2;
    @ProtoField(index = 13)
    public Map<String, Byte> b3;
    @ProtoField(index = 14)
    public Set<Byte> b4;
    @ProtoField(index = 15)
    public List<Byte> b5;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TypeCast)) return false;

        TypeCast typeCast = (TypeCast) o;

        if (a != typeCast.a) return false;
        if (c != typeCast.c) return false;
        if (b != typeCast.b) return false;
        if (a2 != null ? !a2.equals(typeCast.a2) : typeCast.a2 != null) return false;
        if (a3 != null ? !a3.equals(typeCast.a3) : typeCast.a3 != null) return false;
        if (a4 != null ? !a4.equals(typeCast.a4) : typeCast.a4 != null) return false;
        if (a5 != null ? !a5.equals(typeCast.a5) : typeCast.a5 != null) return false;
        if (c2 != null ? !c2.equals(typeCast.c2) : typeCast.c2 != null) return false;
        if (c3 != null ? !c3.equals(typeCast.c3) : typeCast.c3 != null) return false;
        if (c4 != null ? !c4.equals(typeCast.c4) : typeCast.c4 != null) return false;
        if (c5 != null ? !c5.equals(typeCast.c5) : typeCast.c5 != null) return false;
        if (b2 != null ? !b2.equals(typeCast.b2) : typeCast.b2 != null) return false;
        if (b3 != null ? !b3.equals(typeCast.b3) : typeCast.b3 != null) return false;
        if (b4 != null ? !b4.equals(typeCast.b4) : typeCast.b4 != null) return false;
        return b5 != null ? b5.equals(typeCast.b5) : typeCast.b5 == null;
    }

    @Override
    public int hashCode() {
        int result = (int) a;
        result = 31 * result + (a2 != null ? a2.hashCode() : 0);
        result = 31 * result + (a3 != null ? a3.hashCode() : 0);
        result = 31 * result + (a4 != null ? a4.hashCode() : 0);
        result = 31 * result + (a5 != null ? a5.hashCode() : 0);
        result = 31 * result + (int) c;
        result = 31 * result + (c2 != null ? c2.hashCode() : 0);
        result = 31 * result + (c3 != null ? c3.hashCode() : 0);
        result = 31 * result + (c4 != null ? c4.hashCode() : 0);
        result = 31 * result + (c5 != null ? c5.hashCode() : 0);
        result = 31 * result + (int) b;
        result = 31 * result + (b2 != null ? b2.hashCode() : 0);
        result = 31 * result + (b3 != null ? b3.hashCode() : 0);
        result = 31 * result + (b4 != null ? b4.hashCode() : 0);
        result = 31 * result + (b5 != null ? b5.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TypeCast{" +
                "a=" + a +
                ", a2=" + a2 +
                ", a3=" + a3 +
                ", a4=" + a4 +
                ", a5=" + a5 +
                ", c=" + c +
                ", c2=" + c2 +
                ", c3=" + c3 +
                ", c4=" + c4 +
                ", c5=" + c5 +
                ", b=" + b +
                ", b2=" + b2 +
                ", b3=" + b3 +
                ", b4=" + b4 +
                ", b5=" + b5 +
                '}';
    }
}
