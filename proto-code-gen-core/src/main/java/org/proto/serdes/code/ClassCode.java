package org.proto.serdes.code;

import org.proto.serdes.utils.Block;
import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.MultiRowStrings;
import org.proto.serdes.utils.RowString;

import java.util.*;
import java.util.function.Supplier;

public class ClassCode extends AbstractCode<ClassCode> {
    private final String packageName;
    private final int modifier;
    private final String name;
    private final Map<String, MethodDefineCode> name2Method = new HashMap<>();
    private final CodeBody<ImportCode> imports = new CodeBody<>(this);
    private final Map<String, Class<?>> abbreviation2Class = new HashMap<>();
    private final CodeBody<MethodDefineCode> methods = new CodeBody<MethodDefineCode>(this) {
        @Override
        Block newBlock() {
            return new Block(new MultiRowStrings("", "\n\n", false));
        }
    };
    private final Comparator<ImportCode> importComparator = Comparator.comparing(ImportCode::getImportClassName);
    private final Comparator<MethodDefineCode> methodDefComparator = (mc1, mc2) -> {
        if (mc1.getModifier() > mc2.getModifier())
            return 1;
        else if (mc1.getModifier() < mc2.getModifier())
            return -1;
        return mc1.getName().compareTo(mc2.getName());
    };

    public ClassCode(String packageName, int modifier, String name) {
        this.packageName = packageName == null ? "" : packageName;
        this.modifier = modifier;
        this.name = name;
    }

    @Override
    public List<Code> getChildren() {
        List<Code> cs = new ArrayList<>();
        cs.add(imports);
        cs.add(methods);
        return cs;
    }

    public void addImport(Class<?> clazz) {
        abbreviation2Class.putIfAbsent(clazz.getSimpleName(), clazz);
        Package pack = clazz.getPackage();
        if (pack == null || packageName.equals(pack.getName()))
            return;
        if (!imports.exists(ic -> ic.getImportClass().equals(clazz))) {
            ImportCode importCode = new ImportCode(clazz);
            imports.add(importCode);
            importCode.setParent(this);
        }
    }

    boolean hasAbbreviation(Class<?> clazz) {
        Class<?> old = abbreviation2Class.get(clazz.getSimpleName());
        return old != null && old == clazz;
    }

    public MethodDefineCode addMethod(String methodName, Supplier<MethodDefineCode> func) {
        return Optional.ofNullable(name2Method.get(methodName))
                .orElseGet(() -> {
                    MethodDefineCode code = func.get();
                    methods.add(code);
                    code.setParent(ClassCode.this);
                    name2Method.put(methodName, code);
                    return code;
                });
    }

    private void traverseTypeCodes() {
        List<Code> all = new ArrayList<>();
        all.add(this);
        Set<Class<?>> allClasses = new HashSet<>();
        while (!all.isEmpty()) {
            Code code = all.remove(0);
            all.addAll(code.getChildren());
            if (code instanceof TypeCode) {
                allClasses.addAll(((TypeCode) code).getContainingClasses());
            }
        }
        allClasses.forEach(this::addImport);
    }

    @Override
    public Element getContent() {
        traverseTypeCodes();
        RowString declare = MethodDefineCode.addModifier(modifier, new RowString(" "))
                .add("class")
                .add(name)
                .add("{\n");
        return new Block()
                .process(block -> {
                    if (!packageName.trim().isEmpty())
                        block.add("package " + packageName.trim() + ";\n");
                })
                .add(imports.sort(importComparator))
                .add("\n")
                .add(declare)
                .addChild(methods.sort(methodDefComparator))
                .add("\n}");
    }
}
