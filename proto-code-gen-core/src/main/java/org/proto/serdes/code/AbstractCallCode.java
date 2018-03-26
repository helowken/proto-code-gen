package org.proto.serdes.code;

import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.Row;
import org.proto.serdes.utils.RowString;

import java.util.ArrayList;
import java.util.List;

public class AbstractCallCode<T extends Code> extends AbstractCode<T> {
    private List<Code> arguments;

    AbstractCallCode(Object... arguments) {
        List<Code> args = new ArrayList<>();
        if (arguments != null) {
            for (Object arg : arguments) {
                if (arg instanceof Code)
                    args.add((Code) arg);
                else
                    args.add(new VariableCode(arg.toString()));
            }
        }
        this.arguments = args;
    }

    @Override
    public Element getContent() {
        return new Row()
                .add("(")
                .add(new RowString(", ").process(row -> arguments.forEach(row::add)))
                .add(")")
                .add(endfix);
    }
}
