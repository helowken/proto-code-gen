package org.proto.serdes.code;

import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.Row;

public class CastCode extends AbstractCode<CastCode> {
    private final TypeCode type;
    private final Code code;
    private boolean wrapped = false;

    public CastCode(TypeCode type, Code code) {
        this.type = type;
        this.code = code;
    }

    public CastCode(String type, String varName) {
        this(new TypeCode(type), new VariableCode(varName));
    }

    public CastCode wrap() {
        wrapped = true;
        return this;
    }

    @Override
    public Element getContent() {
        String left = "";
        String right = "";
        if (wrapped) {
            left = "(";
            right = ")";
        }
        return new Row()
                .add(left)
                .add("(")
                .add(type)
                .add(") ")
                .add(code)
                .add(right)
                .add(endfix);
    }
}
