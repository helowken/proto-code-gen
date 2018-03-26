package org.proto.serdes.code;

import org.proto.serdes.utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class IfStruct extends CompoundCode<Code, IfStruct> {
    private final boolean forceBrace;
    private final boolean isElse;
    private final ExprCode exprCode;
    private final List<IfStruct> elseIfList = new ArrayList<>();
    private CodeBody<Code> elseBody = new CodeBody<>();

    public IfStruct(ExprCode exprCode) {
        this(exprCode, true);
    }

    public IfStruct(ExprCode exprCode, boolean forceBrace) {
        this(exprCode, forceBrace, false);
    }

    private IfStruct(ExprCode exprCode, boolean forceBrace, boolean isElse) {
        this.exprCode = exprCode;
        this.forceBrace = forceBrace;
        this.isElse = isElse;
    }

    public ExprCode getExprCode() {
        return exprCode;
    }

    public IfStruct addElseIf(ExprCode exprCode, ProcessFunc<IfStruct> func) {
        IfStruct elseIf = new IfStruct(exprCode, forceBrace, true);
        func.apply(elseIf);
        this.elseIfList.add(elseIf);
        return this;
    }

    public IfStruct doElse(ProcessFunc<CodeBody<Code>> func) {
        func.apply(elseBody);
        return this;
    }

    private Pair<String, String> getBraces(CodeBody<?> body) {
        String leftBrace = " {";
        String rightBrace = "} ";
        if (!forceBrace && body.size() == 1) {
            leftBrace = rightBrace = "";
        }
        return new Pair<>(leftBrace, rightBrace);
    }

    private Pair<Element, String> getBasicContent(String prefix) {
        Pair<String, String> braces = getBraces(body);
        String ifClause = isElse ? "else if" : "if";
        Block block = new Block()
                .add(new Row().add(prefix).add(ifClause).add(" (").add(exprCode).add(")").add(braces.left))
                .addChild(body);
        return new Pair<>(block, braces.right);
    }

    @Override
    public Element getContent() {
        AtomicReference<Pair<Element, String>> ref = new AtomicReference<>();
        Pair<Element, String> pair = getBasicContent("");
        ref.set(pair);
        return new Block()
                .add(pair.left)
                .process(block -> elseIfList.forEach(elseIf -> {
                    Pair<Element, String> elseIfPair = elseIf.getBasicContent(ref.get().right);
                    block.add(elseIfPair.left);
                    ref.set(elseIfPair);
                }))
                .process(block -> {
                    if (!elseBody.isEmpty()) {
                        final Pair<String, String> braces = getBraces(elseBody);
                        block.add(new Block().process(childBlock ->
                                childBlock.add(new Row().add(ref.get().right).add("else").add(braces.left))
                                        .addChild(elseBody)
                                        .process(cb -> {
                                            if (!braces.right.isEmpty())
                                                cb.add(braces.right);
                                        })
                        ));
                    } else {
                        String rightBrace = ref.get().right;
                        if (!rightBrace.isEmpty())
                            block.add(rightBrace);
                    }
                });
    }
}
