package org.proto.serdes.code;

import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.Row;
import org.proto.serdes.utils.RowString;

import java.util.*;

public class TypeCode extends AbstractCode<TypeCode> {
    private final Class<?> clazz;
    private final List<TypeCode> args;
    private boolean ellipsis = false;

    public TypeCode(Class<?> clazz, Object... args) {
        this.clazz = clazz;
        List<TypeCode> tmpList = new ArrayList<>();
        if (args != null) {
            for (Object arg : args) {
                if (arg instanceof TypeCode)
                    tmpList.add((TypeCode) arg);
                else
                    tmpList.add(new TypeCode((Class<?>) arg));
            }
        }
        this.args = Collections.unmodifiableList(tmpList);
        for (TypeCode arg : this.args) {
            arg.setParent(this);
        }
    }

    public Set<Class<?>> getContainingClasses() {
        Set<Class<?>> rs = new HashSet<>();
        rs.add(clazz);
        for (TypeCode arg : args) {
            rs.addAll(arg.getContainingClasses());
        }
        return rs;
    }

    public TypeCode toEllipsis() {
        TypeCode tc = new TypeCode(clazz, this.args.toArray(new Object[0]));
        tc.ellipsis = true;
        return tc;
    }

    private String getClassTypeName(Class<?> clazz) {
        Class<?> curr = clazz;
        if (clazz.isMemberClass()) {
            StringBuilder sb = new StringBuilder();
            while (curr != null) {
                if (sb.length() > 0)
                    sb.insert(0, ".");
                sb.insert(0, curr.getSimpleName());
                curr = curr.getDeclaringClass();
            }
            return sb.toString();
        }
        return curr.getSimpleName();
    }


    public List<TypeCode> getArgs() {
        return args;
    }

    @Override
    public Element getContent() {
        boolean useAbbreviation = findAncestorByClass(ClassCode.class)
                .map(classCode -> classCode.hasAbbreviation(clazz))
                .orElse(true);
        Row row = new Row().add(useAbbreviation ? getClassTypeName(clazz) : clazz.getName());
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
