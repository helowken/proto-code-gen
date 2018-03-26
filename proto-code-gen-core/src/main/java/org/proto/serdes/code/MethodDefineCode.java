package org.proto.serdes.code;

import org.proto.serdes.utils.Block;
import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.RowString;

import java.lang.reflect.Modifier;
import java.util.*;

public class MethodDefineCode extends CompoundCode<Code, MethodDefineCode> {
    private final int modifier;
    private final TypeCode returnType;
    private final String name;
    private final List<ParameterCode> params;
    private final Set<String> throwables = new HashSet<>();

    public MethodDefineCode(int modifier, TypeCode returnType, String name, ParameterCode... params) {
        this.modifier = modifier;
        this.name = name;
        this.returnType = returnType;
        this.params = params == null ? Collections.emptyList() : Arrays.asList(params);
    }

    public int getModifier() {
        return modifier;
    }

    public TypeCode getReturnType() {
        return returnType;
    }

    public String getName() {
        return name;
    }

    public List<ParameterCode> getParams() {
        return new ArrayList<>(params);
    }

    public ParameterCode getParamAt(int i) {
        return params.get(i);
    }

    public MethodDefineCode addThrowable(Object... ts) {
        if (ts != null) {
            for (Object t : ts) {
                if (t instanceof Class)
                    throwables.add(((Class<?>) t).getSimpleName());
                else
                    throwables.add(t.toString());
            }
        }
        return this;
    }

    @Override
    public Element getContent() {
        RowString paramRow = new RowString("(", ")", ", ")
                .process(row -> params.forEach(row::add));
        RowString declare = addModifier(modifier, new RowString(" "))
                .add(returnType)
                .add(name + paramRow)
                .process(row -> {
                    if (!throwables.isEmpty())
                        row.add(new RowString("throws ", "", ", ").process(r -> throwables.forEach(r::add)));
                })
                .add("{");
        return new Block()
                .add(declare)
                .addChild(body)
                .add("}");
    }

    static RowString addModifier(int modifier, RowString row) {
        if (Modifier.isPrivate(modifier))
            row.add("private");
        else if (Modifier.isProtected(modifier))
            row.add("protected");
        else if (Modifier.isPublic(modifier))
            row.add("public");
        if (Modifier.isStatic(modifier))
            row.add("static");
        return row;
    }
}
