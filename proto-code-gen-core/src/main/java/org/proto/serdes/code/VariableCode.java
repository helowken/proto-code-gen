package org.proto.serdes.code;

import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.Row;

public class VariableCode extends AbstractCode<VariableCode> {
    private final String name;

    public VariableCode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Element getContent() {
        return new Row().add(name);
    }
}
