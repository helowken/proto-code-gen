package org.proto.serdes.code;

import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.Row;

public class RefCode extends AbstractCode<RefCode> {
    private final Code caller;
    private final VariableCode value;

    public static RefCode create(String caller, String value) {
        return new RefCode(new VariableCode(caller), new VariableCode(value));
    }

    public RefCode(Code caller, VariableCode value) {
        this.caller = caller;
        this.value = value;
    }

    @Override
    public Element getContent() {
        return new Row()
                .add(caller)
                .add(".")
                .add(value)
                .add(endfix);
    }
}
