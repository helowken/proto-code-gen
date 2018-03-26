import com.google.protobuf.InvalidProtocolBufferException;
import java.lang.Byte;
import java.lang.Character;
import java.lang.Short;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.proto.query.ProtoNestType;
import org.proto.query.ProtoTypeCast;
import org.proto.query._InnerMap1;
import org.proto.query._InnerMap2;
import org.proto.query._InnerMap3;
import org.proto.query._InnerRepeated1;
import org.proto.query._InnerRepeated2;
import org.proto.query._InnerRepeated3;
import org.proto.test.entity.NestType;
import org.proto.test.entity.TypeCast;


public class EntityConverter {

    public static ProtoNestType convertTo(NestType oldValue) {
        ProtoNestType.Builder newValue = ProtoNestType.newBuilder();
        Map<String, Integer> v1 = oldValue.getA1();
        if (v1 != null) {
            newValue.putAllA1(new HashMap<>(v1));
        } 
        Map<String, Map<String, Integer>> v2 = oldValue.getA2();
        if (v2 != null) {
            newValue.putAllA2(convertToProtoProtoNestTypeA2(v2));
        } 
        Map<String, List<Integer>> v3 = oldValue.getA3();
        if (v3 != null) {
            newValue.putAllA3(convertToProtoProtoNestTypeA3(v3));
        } 
        Map<String, Map<String, List<Integer>>> v4 = oldValue.getA4();
        if (v4 != null) {
            newValue.putAllA4(convertToProtoProtoNestTypeA4(v4));
        } 
        Map<String, Map<String, Map<String, Integer>>> v5 = oldValue.getA5();
        if (v5 != null) {
            newValue.putAllA5(convertToProtoProtoNestTypeA5(v5));
        } 
        List<Integer> v6 = oldValue.getA6();
        if (v6 != null) {
            newValue.addAllA6(new ArrayList<>(v6));
        } 
        List<Map<String, Integer>> v7 = oldValue.getA7();
        if (v7 != null) {
            newValue.addAllA7(convertToProtoProtoNestTypeA7(v7));
        } 
        List<List<Integer>> v8 = oldValue.getA8();
        if (v8 != null) {
            newValue.addAllA8(convertToProtoProtoNestTypeA8(v8));
        } 
        List<List<List<Integer>>> v9 = oldValue.getA9();
        if (v9 != null) {
            newValue.addAllA9(convertToProtoProtoNestTypeA9(v9));
        } 
        List<List<Map<String, Integer>>> v10 = oldValue.getA10();
        if (v10 != null) {
            newValue.addAllA10(convertToProtoProtoNestTypeA10(v10));
        } 
        Map<String, Integer> v11 = oldValue.a11;
        if (v11 != null) {
            newValue.putAllA11(new HashMap<>(v11));
        } 
        Map<String, Map<String, Integer>> v12 = oldValue.a12;
        if (v12 != null) {
            newValue.putAllA12(convertToProtoProtoNestTypeA12(v12));
        } 
        Map<String, List<Integer>> v13 = oldValue.a13;
        if (v13 != null) {
            newValue.putAllA13(convertToProtoProtoNestTypeA13(v13));
        } 
        Map<String, Map<String, List<Integer>>> v14 = oldValue.a14;
        if (v14 != null) {
            newValue.putAllA14(convertToProtoProtoNestTypeA14(v14));
        } 
        Map<String, Map<String, Map<String, Integer>>> v15 = oldValue.a15;
        if (v15 != null) {
            newValue.putAllA15(convertToProtoProtoNestTypeA15(v15));
        } 
        List<Integer> v16 = oldValue.a16;
        if (v16 != null) {
            newValue.addAllA16(new ArrayList<>(v16));
        } 
        List<Map<String, Integer>> v17 = oldValue.a17;
        if (v17 != null) {
            newValue.addAllA17(convertToProtoProtoNestTypeA17(v17));
        } 
        List<List<Integer>> v18 = oldValue.a18;
        if (v18 != null) {
            newValue.addAllA18(convertToProtoProtoNestTypeA18(v18));
        } 
        List<List<List<Integer>>> v19 = oldValue.a19;
        if (v19 != null) {
            newValue.addAllA19(convertToProtoProtoNestTypeA19(v19));
        } 
        List<List<Map<String, Integer>>> v20 = oldValue.a20;
        if (v20 != null) {
            newValue.addAllA20(convertToProtoProtoNestTypeA20(v20));
        } 
        return newValue.build();
    }

    public static NestType convertTo(ProtoNestType oldValue) {
        NestType newValue = new NestType();
        newValue.setA1(new HashMap<>(oldValue.getA1Map()));
        newValue.setA2(convertToPojoNestTypeA2(oldValue.getA2Map()));
        newValue.setA3(convertToPojoNestTypeA3(oldValue.getA3Map()));
        newValue.setA4(convertToPojoNestTypeA4(oldValue.getA4Map()));
        newValue.setA5(convertToPojoNestTypeA5(oldValue.getA5Map()));
        newValue.setA6(new ArrayList<>(oldValue.getA6List()));
        newValue.setA7(convertToPojoNestTypeA7(oldValue.getA7List()));
        newValue.setA8(convertToPojoNestTypeA8(oldValue.getA8List()));
        newValue.setA9(convertToPojoNestTypeA9(oldValue.getA9List()));
        newValue.setA10(convertToPojoNestTypeA10(oldValue.getA10List()));
        newValue.a11 = new HashMap<>(oldValue.getA11Map());
        newValue.a12 = convertToPojoNestTypeA12(oldValue.getA12Map());
        newValue.a13 = convertToPojoNestTypeA13(oldValue.getA13Map());
        newValue.a14 = convertToPojoNestTypeA14(oldValue.getA14Map());
        newValue.a15 = convertToPojoNestTypeA15(oldValue.getA15Map());
        newValue.a16 = new ArrayList<>(oldValue.getA16List());
        newValue.a17 = convertToPojoNestTypeA17(oldValue.getA17List());
        newValue.a18 = convertToPojoNestTypeA18(oldValue.getA18List());
        newValue.a19 = convertToPojoNestTypeA19(oldValue.getA19List());
        newValue.a20 = convertToPojoNestTypeA20(oldValue.getA20List());
        return newValue;
    }

    public static ProtoTypeCast convertTo(TypeCast oldValue) {
        ProtoTypeCast.Builder newValue = ProtoTypeCast.newBuilder();
        newValue.setA(oldValue.a);
        Short v1 = oldValue.a2;
        if (v1 != null) {
            newValue.setA2(v1.intValue());
        } 
        Map<String, Short> v2 = oldValue.a3;
        if (v2 != null) {
            newValue.putAllA3(convertToProtoProtoTypeCastA3(v2));
        } 
        newValue.setC(oldValue.c);
        Character v3 = oldValue.c2;
        if (v3 != null) {
            newValue.setC2(v3);
        } 
        Map<String, Character> v4 = oldValue.c3;
        if (v4 != null) {
            newValue.putAllC3(convertToProtoProtoTypeCastC3(v4));
        } 
        newValue.setB(oldValue.b);
        Byte v5 = oldValue.b2;
        if (v5 != null) {
            newValue.setB2(v5.intValue());
        } 
        Map<String, Byte> v6 = oldValue.b3;
        if (v6 != null) {
            newValue.putAllB3(convertToProtoProtoTypeCastB3(v6));
        } 
        return newValue.build();
    }

    public static TypeCast convertTo(ProtoTypeCast oldValue) {
        TypeCast newValue = new TypeCast();
        newValue.a = (short) oldValue.getA();
        newValue.a2 = ((Number) oldValue.getA2()).shortValue();
        newValue.a3 = convertToPojoTypeCastA3(oldValue.getA3Map());
        newValue.c = (char) ((Number) oldValue.getC()).byteValue();
        newValue.c2 = (char) ((Number) oldValue.getC2()).byteValue();
        newValue.c3 = convertToPojoTypeCastC3(oldValue.getC3Map());
        newValue.b = (byte) oldValue.getB();
        newValue.b2 = ((Number) oldValue.getB2()).byteValue();
        newValue.b3 = convertToPojoTypeCastB3(oldValue.getB3Map());
        return newValue;
    }

    public static NestType deserializeNestType(byte[] oldValue) throws InvalidProtocolBufferException {
        return convertTo(ProtoNestType.parseFrom(oldValue));
    }

    public static TypeCast deserializeTypeCast(byte[] oldValue) throws InvalidProtocolBufferException {
        return convertTo(ProtoTypeCast.parseFrom(oldValue));
    }

    public static byte[] serialize(NestType oldValue) {
        return convertTo(oldValue).toByteArray();
    }

    public static byte[] serialize(TypeCast oldValue) {
        return convertTo(oldValue).toByteArray();
    }

    private static List<List<Map<String, Integer>>> convertToPojoNestTypeA10(List<_InnerRepeated3> oldValue) {
        List<List<Map<String, Integer>>> newValue = new ArrayList<>();
        for (_InnerRepeated3 v : oldValue) {
            newValue.add(convertToPojo_InnerRepeated3(v));
        }
        return newValue;
    }

    private static Map<String, Map<String, Integer>> convertToPojoNestTypeA12(Map<String, _InnerMap1> oldValue) {
        Map<String, Map<String, Integer>> newValue = new HashMap<>();
        for (Map.Entry<String, _InnerMap1> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), convertToPojo_InnerMap1(entry.getValue()));
        }
        return newValue;
    }

    private static Map<String, List<Integer>> convertToPojoNestTypeA13(Map<String, _InnerRepeated1> oldValue) {
        Map<String, List<Integer>> newValue = new HashMap<>();
        for (Map.Entry<String, _InnerRepeated1> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), convertToPojo_InnerRepeated1(entry.getValue()));
        }
        return newValue;
    }

    private static Map<String, Map<String, List<Integer>>> convertToPojoNestTypeA14(Map<String, _InnerMap2> oldValue) {
        Map<String, Map<String, List<Integer>>> newValue = new HashMap<>();
        for (Map.Entry<String, _InnerMap2> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), convertToPojo_InnerMap2(entry.getValue()));
        }
        return newValue;
    }

    private static Map<String, Map<String, Map<String, Integer>>> convertToPojoNestTypeA15(Map<String, _InnerMap3> oldValue) {
        Map<String, Map<String, Map<String, Integer>>> newValue = new HashMap<>();
        for (Map.Entry<String, _InnerMap3> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), convertToPojo_InnerMap3(entry.getValue()));
        }
        return newValue;
    }

    private static List<Map<String, Integer>> convertToPojoNestTypeA17(List<_InnerMap1> oldValue) {
        List<Map<String, Integer>> newValue = new ArrayList<>();
        for (_InnerMap1 v : oldValue) {
            newValue.add(convertToPojo_InnerMap1(v));
        }
        return newValue;
    }

    private static List<List<Integer>> convertToPojoNestTypeA18(List<_InnerRepeated1> oldValue) {
        List<List<Integer>> newValue = new ArrayList<>();
        for (_InnerRepeated1 v : oldValue) {
            newValue.add(convertToPojo_InnerRepeated1(v));
        }
        return newValue;
    }

    private static List<List<List<Integer>>> convertToPojoNestTypeA19(List<_InnerRepeated2> oldValue) {
        List<List<List<Integer>>> newValue = new ArrayList<>();
        for (_InnerRepeated2 v : oldValue) {
            newValue.add(convertToPojo_InnerRepeated2(v));
        }
        return newValue;
    }

    private static Map<String, Map<String, Integer>> convertToPojoNestTypeA2(Map<String, _InnerMap1> oldValue) {
        Map<String, Map<String, Integer>> newValue = new HashMap<>();
        for (Map.Entry<String, _InnerMap1> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), convertToPojo_InnerMap1(entry.getValue()));
        }
        return newValue;
    }

    private static List<List<Map<String, Integer>>> convertToPojoNestTypeA20(List<_InnerRepeated3> oldValue) {
        List<List<Map<String, Integer>>> newValue = new ArrayList<>();
        for (_InnerRepeated3 v : oldValue) {
            newValue.add(convertToPojo_InnerRepeated3(v));
        }
        return newValue;
    }

    private static Map<String, List<Integer>> convertToPojoNestTypeA3(Map<String, _InnerRepeated1> oldValue) {
        Map<String, List<Integer>> newValue = new HashMap<>();
        for (Map.Entry<String, _InnerRepeated1> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), convertToPojo_InnerRepeated1(entry.getValue()));
        }
        return newValue;
    }

    private static Map<String, Map<String, List<Integer>>> convertToPojoNestTypeA4(Map<String, _InnerMap2> oldValue) {
        Map<String, Map<String, List<Integer>>> newValue = new HashMap<>();
        for (Map.Entry<String, _InnerMap2> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), convertToPojo_InnerMap2(entry.getValue()));
        }
        return newValue;
    }

    private static Map<String, Map<String, Map<String, Integer>>> convertToPojoNestTypeA5(Map<String, _InnerMap3> oldValue) {
        Map<String, Map<String, Map<String, Integer>>> newValue = new HashMap<>();
        for (Map.Entry<String, _InnerMap3> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), convertToPojo_InnerMap3(entry.getValue()));
        }
        return newValue;
    }

    private static List<Map<String, Integer>> convertToPojoNestTypeA7(List<_InnerMap1> oldValue) {
        List<Map<String, Integer>> newValue = new ArrayList<>();
        for (_InnerMap1 v : oldValue) {
            newValue.add(convertToPojo_InnerMap1(v));
        }
        return newValue;
    }

    private static List<List<Integer>> convertToPojoNestTypeA8(List<_InnerRepeated1> oldValue) {
        List<List<Integer>> newValue = new ArrayList<>();
        for (_InnerRepeated1 v : oldValue) {
            newValue.add(convertToPojo_InnerRepeated1(v));
        }
        return newValue;
    }

    private static List<List<List<Integer>>> convertToPojoNestTypeA9(List<_InnerRepeated2> oldValue) {
        List<List<List<Integer>>> newValue = new ArrayList<>();
        for (_InnerRepeated2 v : oldValue) {
            newValue.add(convertToPojo_InnerRepeated2(v));
        }
        return newValue;
    }

    private static Map<String, Short> convertToPojoTypeCastA3(Map<String, Integer> oldValue) {
        Map<String, Short> newValue = new HashMap<>();
        for (Map.Entry<String, Integer> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), entry.getValue().shortValue());
        }
        return newValue;
    }

    private static Map<String, Byte> convertToPojoTypeCastB3(Map<String, Integer> oldValue) {
        Map<String, Byte> newValue = new HashMap<>();
        for (Map.Entry<String, Integer> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), entry.getValue().byteValue());
        }
        return newValue;
    }

    private static Map<String, Character> convertToPojoTypeCastC3(Map<String, Integer> oldValue) {
        Map<String, Character> newValue = new HashMap<>();
        for (Map.Entry<String, Integer> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), (char) entry.getValue().byteValue());
        }
        return newValue;
    }

    private static Map<String, Integer> convertToPojo_InnerMap1(_InnerMap1 oldValue) {
        Map<String, Integer> newValue = new HashMap<>();
        newValue.putAll(new HashMap<>(oldValue.getValueMap()));
        return newValue;
    }

    private static Map<String, List<Integer>> convertToPojo_InnerMap2(_InnerMap2 oldValue) {
        Map<String, List<Integer>> newValue = new HashMap<>();
        newValue.putAll(convertToPojo_InnerMap2Value(oldValue.getValueMap()));
        return newValue;
    }

    private static Map<String, List<Integer>> convertToPojo_InnerMap2Value(Map<String, _InnerRepeated1> oldValue) {
        Map<String, List<Integer>> newValue = new HashMap<>();
        for (Map.Entry<String, _InnerRepeated1> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), convertToPojo_InnerRepeated1(entry.getValue()));
        }
        return newValue;
    }

    private static Map<String, Map<String, Integer>> convertToPojo_InnerMap3(_InnerMap3 oldValue) {
        Map<String, Map<String, Integer>> newValue = new HashMap<>();
        newValue.putAll(convertToPojo_InnerMap3Value(oldValue.getValueMap()));
        return newValue;
    }

    private static Map<String, Map<String, Integer>> convertToPojo_InnerMap3Value(Map<String, _InnerMap1> oldValue) {
        Map<String, Map<String, Integer>> newValue = new HashMap<>();
        for (Map.Entry<String, _InnerMap1> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), convertToPojo_InnerMap1(entry.getValue()));
        }
        return newValue;
    }

    private static List<Integer> convertToPojo_InnerRepeated1(_InnerRepeated1 oldValue) {
        List<Integer> newValue = new ArrayList<>();
        newValue.addAll(new ArrayList<>(oldValue.getValueList()));
        return newValue;
    }

    private static List<List<Integer>> convertToPojo_InnerRepeated2(_InnerRepeated2 oldValue) {
        List<List<Integer>> newValue = new ArrayList<>();
        newValue.addAll(convertToPojo_InnerRepeated2Value(oldValue.getValueList()));
        return newValue;
    }

    private static List<List<Integer>> convertToPojo_InnerRepeated2Value(List<_InnerRepeated1> oldValue) {
        List<List<Integer>> newValue = new ArrayList<>();
        for (_InnerRepeated1 v : oldValue) {
            newValue.add(convertToPojo_InnerRepeated1(v));
        }
        return newValue;
    }

    private static List<Map<String, Integer>> convertToPojo_InnerRepeated3(_InnerRepeated3 oldValue) {
        List<Map<String, Integer>> newValue = new ArrayList<>();
        newValue.addAll(convertToPojo_InnerRepeated3Value(oldValue.getValueList()));
        return newValue;
    }

    private static List<Map<String, Integer>> convertToPojo_InnerRepeated3Value(List<_InnerMap1> oldValue) {
        List<Map<String, Integer>> newValue = new ArrayList<>();
        for (_InnerMap1 v : oldValue) {
            newValue.add(convertToPojo_InnerMap1(v));
        }
        return newValue;
    }

    private static List<_InnerRepeated3> convertToProtoProtoNestTypeA10(List<List<Map<String, Integer>>> oldValue) {
        List<_InnerRepeated3> newValue = new ArrayList<>();
        for (List<Map<String, Integer>> v : oldValue) {
            newValue.add(convertToProto_InnerRepeated3(v));
        }
        return newValue;
    }

    private static Map<String, _InnerMap1> convertToProtoProtoNestTypeA12(Map<String, Map<String, Integer>> oldValue) {
        Map<String, _InnerMap1> newValue = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), convertToProto_InnerMap1(entry.getValue()));
        }
        return newValue;
    }

    private static Map<String, _InnerRepeated1> convertToProtoProtoNestTypeA13(Map<String, List<Integer>> oldValue) {
        Map<String, _InnerRepeated1> newValue = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), convertToProto_InnerRepeated1(entry.getValue()));
        }
        return newValue;
    }

    private static Map<String, _InnerMap2> convertToProtoProtoNestTypeA14(Map<String, Map<String, List<Integer>>> oldValue) {
        Map<String, _InnerMap2> newValue = new HashMap<>();
        for (Map.Entry<String, Map<String, List<Integer>>> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), convertToProto_InnerMap2(entry.getValue()));
        }
        return newValue;
    }

    private static Map<String, _InnerMap3> convertToProtoProtoNestTypeA15(Map<String, Map<String, Map<String, Integer>>> oldValue) {
        Map<String, _InnerMap3> newValue = new HashMap<>();
        for (Map.Entry<String, Map<String, Map<String, Integer>>> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), convertToProto_InnerMap3(entry.getValue()));
        }
        return newValue;
    }

    private static List<_InnerMap1> convertToProtoProtoNestTypeA17(List<Map<String, Integer>> oldValue) {
        List<_InnerMap1> newValue = new ArrayList<>();
        for (Map<String, Integer> v : oldValue) {
            newValue.add(convertToProto_InnerMap1(v));
        }
        return newValue;
    }

    private static List<_InnerRepeated1> convertToProtoProtoNestTypeA18(List<List<Integer>> oldValue) {
        List<_InnerRepeated1> newValue = new ArrayList<>();
        for (List<Integer> v : oldValue) {
            newValue.add(convertToProto_InnerRepeated1(v));
        }
        return newValue;
    }

    private static List<_InnerRepeated2> convertToProtoProtoNestTypeA19(List<List<List<Integer>>> oldValue) {
        List<_InnerRepeated2> newValue = new ArrayList<>();
        for (List<List<Integer>> v : oldValue) {
            newValue.add(convertToProto_InnerRepeated2(v));
        }
        return newValue;
    }

    private static Map<String, _InnerMap1> convertToProtoProtoNestTypeA2(Map<String, Map<String, Integer>> oldValue) {
        Map<String, _InnerMap1> newValue = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), convertToProto_InnerMap1(entry.getValue()));
        }
        return newValue;
    }

    private static List<_InnerRepeated3> convertToProtoProtoNestTypeA20(List<List<Map<String, Integer>>> oldValue) {
        List<_InnerRepeated3> newValue = new ArrayList<>();
        for (List<Map<String, Integer>> v : oldValue) {
            newValue.add(convertToProto_InnerRepeated3(v));
        }
        return newValue;
    }

    private static Map<String, _InnerRepeated1> convertToProtoProtoNestTypeA3(Map<String, List<Integer>> oldValue) {
        Map<String, _InnerRepeated1> newValue = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), convertToProto_InnerRepeated1(entry.getValue()));
        }
        return newValue;
    }

    private static Map<String, _InnerMap2> convertToProtoProtoNestTypeA4(Map<String, Map<String, List<Integer>>> oldValue) {
        Map<String, _InnerMap2> newValue = new HashMap<>();
        for (Map.Entry<String, Map<String, List<Integer>>> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), convertToProto_InnerMap2(entry.getValue()));
        }
        return newValue;
    }

    private static Map<String, _InnerMap3> convertToProtoProtoNestTypeA5(Map<String, Map<String, Map<String, Integer>>> oldValue) {
        Map<String, _InnerMap3> newValue = new HashMap<>();
        for (Map.Entry<String, Map<String, Map<String, Integer>>> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), convertToProto_InnerMap3(entry.getValue()));
        }
        return newValue;
    }

    private static List<_InnerMap1> convertToProtoProtoNestTypeA7(List<Map<String, Integer>> oldValue) {
        List<_InnerMap1> newValue = new ArrayList<>();
        for (Map<String, Integer> v : oldValue) {
            newValue.add(convertToProto_InnerMap1(v));
        }
        return newValue;
    }

    private static List<_InnerRepeated1> convertToProtoProtoNestTypeA8(List<List<Integer>> oldValue) {
        List<_InnerRepeated1> newValue = new ArrayList<>();
        for (List<Integer> v : oldValue) {
            newValue.add(convertToProto_InnerRepeated1(v));
        }
        return newValue;
    }

    private static List<_InnerRepeated2> convertToProtoProtoNestTypeA9(List<List<List<Integer>>> oldValue) {
        List<_InnerRepeated2> newValue = new ArrayList<>();
        for (List<List<Integer>> v : oldValue) {
            newValue.add(convertToProto_InnerRepeated2(v));
        }
        return newValue;
    }

    private static Map<String, Integer> convertToProtoProtoTypeCastA3(Map<String, Short> oldValue) {
        Map<String, Integer> newValue = new HashMap<>();
        for (Map.Entry<String, Short> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), entry.getValue().intValue());
        }
        return newValue;
    }

    private static Map<String, Integer> convertToProtoProtoTypeCastB3(Map<String, Byte> oldValue) {
        Map<String, Integer> newValue = new HashMap<>();
        for (Map.Entry<String, Byte> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), entry.getValue().intValue());
        }
        return newValue;
    }

    private static Map<String, Integer> convertToProtoProtoTypeCastC3(Map<String, Character> oldValue) {
        Map<String, Integer> newValue = new HashMap<>();
        for (Map.Entry<String, Character> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), (int) entry.getValue());
        }
        return newValue;
    }

    private static _InnerMap1 convertToProto_InnerMap1(Map<String, Integer> oldValue) {
        _InnerMap1.Builder newValue = _InnerMap1.newBuilder();
        if (oldValue != null) {
            newValue.putAllValue(new HashMap<>(oldValue));
        } 
        return newValue.build();
    }

    private static _InnerMap2 convertToProto_InnerMap2(Map<String, List<Integer>> oldValue) {
        _InnerMap2.Builder newValue = _InnerMap2.newBuilder();
        if (oldValue != null) {
            newValue.putAllValue(convertToProto_InnerMap2Value(oldValue));
        } 
        return newValue.build();
    }

    private static Map<String, _InnerRepeated1> convertToProto_InnerMap2Value(Map<String, List<Integer>> oldValue) {
        Map<String, _InnerRepeated1> newValue = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), convertToProto_InnerRepeated1(entry.getValue()));
        }
        return newValue;
    }

    private static _InnerMap3 convertToProto_InnerMap3(Map<String, Map<String, Integer>> oldValue) {
        _InnerMap3.Builder newValue = _InnerMap3.newBuilder();
        if (oldValue != null) {
            newValue.putAllValue(convertToProto_InnerMap3Value(oldValue));
        } 
        return newValue.build();
    }

    private static Map<String, _InnerMap1> convertToProto_InnerMap3Value(Map<String, Map<String, Integer>> oldValue) {
        Map<String, _InnerMap1> newValue = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> entry : oldValue.entrySet()) {
            newValue.put(entry.getKey(), convertToProto_InnerMap1(entry.getValue()));
        }
        return newValue;
    }

    private static _InnerRepeated1 convertToProto_InnerRepeated1(List<Integer> oldValue) {
        _InnerRepeated1.Builder newValue = _InnerRepeated1.newBuilder();
        if (oldValue != null) {
            newValue.addAllValue(new ArrayList<>(oldValue));
        } 
        return newValue.build();
    }

    private static _InnerRepeated2 convertToProto_InnerRepeated2(List<List<Integer>> oldValue) {
        _InnerRepeated2.Builder newValue = _InnerRepeated2.newBuilder();
        if (oldValue != null) {
            newValue.addAllValue(convertToProto_InnerRepeated2Value(oldValue));
        } 
        return newValue.build();
    }

    private static List<_InnerRepeated1> convertToProto_InnerRepeated2Value(List<List<Integer>> oldValue) {
        List<_InnerRepeated1> newValue = new ArrayList<>();
        for (List<Integer> v : oldValue) {
            newValue.add(convertToProto_InnerRepeated1(v));
        }
        return newValue;
    }

    private static _InnerRepeated3 convertToProto_InnerRepeated3(List<Map<String, Integer>> oldValue) {
        _InnerRepeated3.Builder newValue = _InnerRepeated3.newBuilder();
        if (oldValue != null) {
            newValue.addAllValue(convertToProto_InnerRepeated3Value(oldValue));
        } 
        return newValue.build();
    }

    private static List<_InnerMap1> convertToProto_InnerRepeated3Value(List<Map<String, Integer>> oldValue) {
        List<_InnerMap1> newValue = new ArrayList<>();
        for (Map<String, Integer> v : oldValue) {
            newValue.add(convertToProto_InnerMap1(v));
        }
        return newValue;
    }

}