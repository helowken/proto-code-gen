package org.proto.serdes.code;

import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.Row;

public class NewCode extends AbstractCallCode<NewCode> {
    private TypeCode type;

    public NewCode(String typeName, Object... arguments) {
        this(new TypeCode(typeName), arguments);
    }

    public NewCode(TypeCode type, Object... arguments) {
        super(arguments);
        this.type = type.toEllipsis();
    }

    @Override
    public Element getContent() {
        return new Row().add("new").add(" ").add(type).add(super.getContent());
    }

    public static void main(String[] args) {
        System.out.println(new NewCode(new TypeCode("A", "aaa").toEllipsis()));
    }
}
