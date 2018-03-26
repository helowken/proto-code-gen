package org.proto.serdes.code;

import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.Row;
import org.proto.serdes.utils.RowString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TypeCode extends AbstractCode<TypeCode> {
    private final String className;
    private final List<TypeCode> args;
    private boolean ellipsis = false;

    public TypeCode(String className, Object... args) {
        this.className = className;
        List<TypeCode> tmpList = new ArrayList<>();
        if (args != null) {
            for (Object arg : args) {
                if (arg instanceof TypeCode)
                    tmpList.add((TypeCode) arg);
                else
                    tmpList.add(new TypeCode(arg.toString()));
            }
        }
        this.args = Collections.unmodifiableList(tmpList);
    }

    public TypeCode toEllipsis() {
        TypeCode tc = new TypeCode(className, this.args.toArray(new Object[0]));
        tc.ellipsis = true;
        return tc;
    }

    public List<TypeCode> getArgs() {
        return args;
    }

    @Override
    public Element getContent() {
        Row row = new Row().add(className);
        if (!args.isEmpty()) {
            row.add("<")
                    .process(ro -> {
                        if (!ellipsis)
                            ro.add(new Row(new RowString(", ")).process(r -> {
                                for (Code code : args) {
                                    r.add(code);
                                }
                            }));
                    })
                    .add(">");
        }
        return row;
    }
}
