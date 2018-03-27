package org.proto.serdes.code;

import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.Row;

import java.util.Arrays;
import java.util.List;

public class CastCode extends AbstractCode<CastCode> {
    private final TypeCode type;
    private final Code code;
    private boolean wrapped = false;

    public CastCode(TypeCode type, Code code) {
        this.type = type;
        this.code = code;
        type.setParent(this);
        code.setParent(this);
    }

    public CastCode(Class<?> clazz, String varName) {
        this(new TypeCode(clazz), new VariableCode(varName));
    }

    public CastCode wrap() {
        wrapped = true;
        return this;
    }

    @Override
    public List<Code> getChildren() {
        return Arrays.asList(type, code);
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
