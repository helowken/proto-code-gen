package org.proto.test;

import org.junit.Test;

public class SerdesCT {
//    @Test
//    public void test1() throws Throwable {
//        A origin = new A();
//        origin.setA1(newMap());
//        origin.setA2(newMap(ProtoCTUtils::newMap));
//        origin.setA3(newMap(ProtoCTUtils::newList));
//        origin.setA4(newMap(() -> newMap(ProtoCTUtils::newList)));
//        origin.setA5(newMap(() -> newMap(ProtoCTUtils::newMap)));
//        origin.setA6(newList());
//        origin.setA7(newList(ProtoCTUtils::newMap));
//        origin.setA8(newList(ProtoCTUtils::newList));
//        origin.setA9(newList(() -> newList(ProtoCTUtils::newList)));
//        origin.setA10(newList(() -> newList(ProtoCTUtils::newMap)));
//
//        origin.a11 = newMap();
//        origin.a12 = newMap(ProtoCTUtils::newMap);
//        origin.a13 = newMap(ProtoCTUtils::newList);
//        origin.a14 = newMap(() -> newMap(ProtoCTUtils::newList));
//        origin.a15 = newMap(() -> newMap(ProtoCTUtils::newMap));
//        origin.a16 = newList();
//        origin.a17 = newList(ProtoCTUtils::newMap);
//        origin.a18 = newList(ProtoCTUtils::newList);
//        origin.a19 = newList(() -> newList(ProtoCTUtils::newList));
//        origin.a20 = newList(() -> newList(ProtoCTUtils::newMap));
//        printLine();
//        printJson(origin);
//        printLine();
//
//        ProtoSerdes<A> serdes = ProtoSerdesFactory.getInstance().getSerdes(A.class);
//        byte[] bs = serdes.serialize(origin);
//        A to = serdes.deserialize(bs);
//        Assert.assertEquals(to, origin);
//        ProtoA protoA = (ProtoA) serdes.toProto(origin);
//        to = serdes.toPojo(protoA);
//        Assert.assertEquals(to, origin);
//
//        bs = TestConverter.serialize(origin);
//        to = TestConverter.deserializeA(bs);
//        Assert.assertEquals(to, origin);
//        protoA = TestConverter.convertTo(origin);
//        to = TestConverter.convertTo(protoA);
//        Assert.assertEquals(to, origin);
//    }
}
