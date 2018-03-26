package org.proto.serdes.code;

import org.proto.serdes.utils.Block;
import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.RowString;

public class ForStruct extends CompoundCode<Code, ForStruct> {
    private final Code left;
    private final Code right;

    public ForStruct(Code left, Code right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Element getContent() {
        RowString declare = new RowString("(", ")", " ")
                .add(left)
                .add(":")
                .add(right);
        return new Block()
                .add("for " + declare + " {")
                .addChild(body)
                .add("}");
    }

    public static class ForMapEntry extends ForStruct {
        public ForMapEntry(TypeCode keyType, TypeCode valueType, String paramName, String iterableSource) {
            super(
                    new ParameterCode(new TypeCode("Map.Entry", keyType, valueType), paramName),
                    MethodCallCode.create(iterableSource, "entrySet")
            );
        }
    }
}
