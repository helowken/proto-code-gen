package org.proto.test.entity;

import org.proto.serdes.annotation.ProtoClass;
import org.proto.serdes.annotation.ProtoField;

import java.util.Map;

@ProtoClass(protoClass = "ProtoTypeCast")
public class TypeCast {
    @ProtoField(index = 1)
    public short a;
    @ProtoField(index = 2)
    public Short a2;
    @ProtoField(index = 3)
    public Map<String, Short> a3;

    @ProtoField(index = 4)
    public char c;
    @ProtoField(index = 5)
    public Character c2;
    @ProtoField(index = 6)
    public Map<String, Character> c3;

    @ProtoField(index = 7)
    public byte b;
    @ProtoField(index = 8)
    public Byte b2;
    @ProtoField(index = 9)
    public Map<String, Byte> b3;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TypeCast)) return false;

        TypeCast old = (TypeCast) o;

        if (a != old.a) return false;
        if (c != old.c) return false;
        if (b != old.b) return false;
        if (!a2.equals(old.a2)) return false;
        if (!a3.equals(old.a3)) return false;
        if (!c2.equals(old.c2)) return false;
        if (!c3.equals(old.c3)) return false;
        if (!b2.equals(old.b2)) return false;
        return b3.equals(old.b3);
    }

    @Override
    public int hashCode() {
        int result = (int) a;
        result = 31 * result + a2.hashCode();
        result = 31 * result + a3.hashCode();
        result = 31 * result + (int) c;
        result = 31 * result + c2.hashCode();
        result = 31 * result + c3.hashCode();
        result = 31 * result + (int) b;
        result = 31 * result + b2.hashCode();
        result = 31 * result + b3.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TypeCast{" +
                "\na=" + a +
                ", \na2=" + a2 +
                ", \na3=" + a3 +
                ", \nc=" + c +
                ", \nc2=" + c2 +
                ", \nc3=" + c3 +
                ", \nb=" + b +
                ", \nb2=" + b2 +
                ", \nb3=" + b3 +
                "\n}";
    }
}
