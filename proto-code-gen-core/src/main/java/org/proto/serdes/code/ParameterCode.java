package org.proto.serdes.code;

import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.Row;

public class ParameterCode extends AbstractCode<ParameterCode> {
    private final TypeCode type;
    private final String name;

    public ParameterCode(String typeName, String name) {
        this(new TypeCode(typeName), name);
    }

    public ParameterCode(TypeCode type, String name) {
        this.type = type;
        this.name = name;
    }

    public TypeCode getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public Element getContent() {
        return new Row()
                .add(type)
                .add(" ")
                .add(name)
                .add(endfix);
    }
}
