package org.proto.test;

import com.google.protobuf.MessageLite;
import org.junit.Test;
import org.proto.query.ProtoNestType;
import org.proto.serdes.ProtoSerdes;
import org.proto.serdes.ProtoSerdesFactory;
import org.proto.test.entity.NestType;
import org.proto.test.entity.TypeCast;

import static org.junit.Assert.assertEquals;
import static org.proto.test.ProtoCTUtils.*;

public class SerdesCT {
    @Test
    public void testNestType() throws Throwable {
        NestType origin = new NestType();
        origin.setA1(newMap());
        origin.setA2(newMap(ProtoCTUtils::newMap));
        origin.setA3(newMap(ProtoCTUtils::newList));
        origin.setA4(newMap(() -> newMap(ProtoCTUtils::newList)));
        origin.setA5(newMap(() -> newMap(ProtoCTUtils::newMap)));
        origin.setA6(newList());
        origin.setA7(newList(ProtoCTUtils::newMap));
        origin.setA8(newList(ProtoCTUtils::newList));
        origin.setA9(newList(() -> newList(ProtoCTUtils::newList)));
        origin.setA10(newList(() -> newList(ProtoCTUtils::newMap)));

        origin.a11 = newMap();
        origin.a12 = newMap(ProtoCTUtils::newMap);
        origin.a13 = newMap(ProtoCTUtils::newList);
        origin.a14 = newMap(() -> newMap(ProtoCTUtils::newList));
        origin.a15 = newMap(() -> newMap(ProtoCTUtils::newMap));
        origin.a16 = newList();
        origin.a17 = newList(ProtoCTUtils::newMap);
        origin.a18 = newList(ProtoCTUtils::newList);
        origin.a19 = newList(() -> newList(ProtoCTUtils::newList));
        origin.a20 = newList(() -> newList(ProtoCTUtils::newMap));
//        printLine();
//        printJson(origin);
//        printLine();

        check(origin);

        byte[] bs = EntityConverter.serialize(origin);
        NestType to = EntityConverter.deserializeNestType(bs);
        assertEquals(to, origin);
        ProtoNestType protoA = EntityConverter.convertTo(origin);
        to = EntityConverter.convertTo(protoA);
        assertEquals(to, origin);
    }

    @Test
    public void testTypeCast() throws Throwable {
        TypeCast old = new TypeCast();
        old.a = 3;
        old.a2 = 3;
        old.a3 = newShortMap();

        old.c = 'c';
        old.c2 = 'c';
        old.c3 = newCharMap();

        old.b = 2;
        old.b2 = 2;
        old.b3 = newByteMap();

        check(old);

        byte[] bs = EntityConverter.serialize(old);
        TypeCast to = EntityConverter.deserializeTypeCast(bs);
        assertEquals(to, old);
        to = EntityConverter.convertTo(EntityConverter.convertTo(old));
        assertEquals(to, old);
    }

    private <T, K extends MessageLite> void check(T origin) throws Throwable {
        ProtoSerdes<T> serdes = ProtoSerdesFactory.getInstance().getSerdes((Class<T>) origin.getClass());
        byte[] bs = serdes.serialize(origin);
        T to = serdes.deserialize(bs);
        assertEquals(to, origin);
        K protoA = (K) serdes.toProto(origin);
        to = serdes.toPojo(protoA);
        assertEquals(to, origin);
    }

}
