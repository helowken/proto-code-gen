package org.proto.serdes.code;

import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.Row;

import java.util.ArrayList;
import java.util.List;

public class NewCode extends AbstractCallCode<NewCode> {
    private TypeCode type;

    public NewCode(Class<?> clazz, Object... arguments) {
        this(new TypeCode(clazz), arguments);
    }

    public NewCode(TypeCode type, Object... arguments) {
        super(arguments);
        this.type = type.toEllipsis();
        type.setParent(this);
    }

    @Override
    public List<Code> getChildren() {
        List<Code> cs = new ArrayList<>(super.getChildren());
        cs.add(type);
        return cs;
    }

    @Override
    public Element getContent() {
        return new Row().add("new").add(" ").add(type).add(super.getContent());
    }

}
