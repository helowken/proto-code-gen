package org.proto.serdes.utils;

public class Row implements Element<Row> {
    private RowString row;

    public Row() {
        this(new RowString(""));
    }

    public Row(RowString row) {
        this.row = row;
    }

    @Override
    public Object getElementContent() {
        return row;
    }

    @Override
    public Row add(Object v) {
        row.add(convert(v));
        return this;
    }

    @Override
    public String toString() {
        return row.toString();
    }
}
