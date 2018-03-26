package org.proto.serdes.code;

import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.Row;

public class ReturnCode extends AbstractCode<ReturnCode> {
    private final Code value;

    public ReturnCode(String value) {
        this(new VariableCode(value));
    }

    public ReturnCode(Code value) {
        this.value = value;
    }

    @Override
    public Element getContent() {
        return new Row()
                .add("return")
                .add(" ")
                .add(value)
                .add(endfix);
    }
}
