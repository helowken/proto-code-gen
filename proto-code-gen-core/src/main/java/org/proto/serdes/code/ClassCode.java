package org.proto.serdes.code;

import org.proto.serdes.utils.Block;
import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.MultiRowStrings;
import org.proto.serdes.utils.RowString;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ClassCode extends AbstractCode<ClassCode> {
    private final String packageName;
    private final int modifer;
    private final String name;
    private final Map<String, MethodDefineCode> name2Method = new HashMap<>();
    private final CodeBody<ImportCode> imports = new CodeBody<>();
    private final CodeBody<MethodDefineCode> methods = new CodeBody<MethodDefineCode>() {
        @Override
        Block newBlock() {
            return new Block(new MultiRowStrings("", "\n\n", false));
        }
    };
    private final Comparator<ImportCode> importComparator = Comparator.comparing(ImportCode::getClassName);
    private final Comparator<MethodDefineCode> methodDefComparator = (mc1, mc2) -> {
        if (mc1.getModifier() > mc2.getModifier())
            return 1;
        else if (mc1.getModifier() < mc2.getModifier())
            return -1;
        return mc1.getName().compareTo(mc2.getName());
    };

    public ClassCode(String packageName, int modifer, String name) {
        this.packageName = packageName;
        this.modifer = modifer;
        this.name = name;
    }

    public void addImport(Class<?> clazz) {
        addImport(clazz.getName());
    }

    public void addImport(String className) {
        int pos = className.lastIndexOf(".");
        if (pos > -1) {
            String packName = className.substring(0, pos);
            String simpleName = className.substring(pos + 1);
            if (packName.equals(packageName) && !simpleName.contains("."))
                return;
        }
        if (!imports.exists(ic -> ic.getClassName().equals(className)))
            imports.add(new ImportCode(className));
    }

    public MethodDefineCode addMethod(String methodName, Supplier<MethodDefineCode> func) {
        return Optional.ofNullable(name2Method.get(methodName))
                .orElseGet(() -> {
                    MethodDefineCode code = func.get();
                    methods.add(code);
                    name2Method.put(methodName, code);
                    return code;
                });
    }

    @Override
    public Element getContent() {
        RowString declare = MethodDefineCode.addModifier(modifer, new RowString(" "))
                .add("class")
                .add(name)
                .add("{\n");
        return new Block()
                .process(block -> {
                    if (packageName != null && !packageName.trim().isEmpty())
                        block.add("package " + packageName.trim() + ";\n");
                })
                .add(imports.sort(importComparator))
                .add("\n")
                .add(declare)
                .addChild(methods.sort(methodDefComparator))
                .add("\n}");
    }
}
