package org.proto.serdes.code;

import org.proto.serdes.utils.Block;
import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.RowString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ForStruct extends CompoundCode<Code, ForStruct> {
    private final Code left;
    private final Code right;

    public ForStruct(Code left, Code right) {
        this.left = left;
        this.right = right;
        left.setParent(this);
        right.setParent(this);
    }

    @Override
    public List<Code> getChildren() {
        List<Code> cs = new ArrayList<>(super.getChildren());
        cs.add(left);
        cs.add(right);
        return cs;
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
                    new ParameterCode(new TypeCode(Map.Entry.class, keyType, valueType), paramName),
                    MethodCallCode.create(iterableSource, "entrySet")
            );
        }
    }
}
