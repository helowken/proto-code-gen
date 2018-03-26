package org.proto.serdes.utils;

import org.proto.serdes.ProtoConst;

public class Block implements Element<Block> {
    private MultiRowStrings rows;
    private MultiRowStrings childRows;

    public Block() {
        this(new MultiRowStrings("", "\n", false));
    }

    public Block(MultiRowStrings rows) {
        this.rows = rows;
    }

    private MultiRowStrings getChildRows() {
        if (childRows == null)
            childRows = rows.newChildBlock(ProtoConst.INDENT, "\n", false);
        return childRows;
    }

    @Override
    public Block add(Object v) {
        rows.add(convert(v));
        return this;
    }

    public Block addChild(Object v) {
        getChildRows().add(convert(v));
        return this;
    }

    public Object getElementContent() {
        return rows;
    }

    @Override
    public String toString() {
        return rows.toString();
    }
}
