package org.proto.serdes.code;

import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.Row;

import java.util.ArrayList;
import java.util.List;

public class MethodCallCode extends AbstractCallCode<MethodCallCode> {
    private final Code caller;
    private final String methodName;

    public static MethodCallCode create(String caller, String methodName, Object... arguments) {
        return new MethodCallCode(caller == null ? null : new VariableCode(caller), methodName, arguments);
    }

    public MethodCallCode(Code caller, String methodName, Object... arguments) {
        super(arguments);
        this.caller = caller;
        this.methodName = methodName;
        if (caller != null)
            caller.setParent(this);
    }

    @Override
    public Element getContent() {
        return new Row()
                .add(caller == null ? "" : caller + ".")
                .add(methodName)
                .add(super.getContent());
    }

    @Override
    public List<Code> getChildren() {
        List<Code> cs = new ArrayList<>(super.getChildren());
        if (caller != null)
            cs.add(caller);
        return cs;
    }
}
