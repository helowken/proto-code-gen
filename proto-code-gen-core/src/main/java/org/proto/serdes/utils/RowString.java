package org.proto.serdes.utils;

public class RowString extends AbstractRichString<RowString> {
    private String sep;

    public RowString(String sep) {
        this("", "", sep);
    }

    public RowString(String prefix, String suffix, String sep) {
        super(prefix, suffix);
        this.sep = sep;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        for (int i = 0, len = children.size(); i < len; ++i) {
            if (i > 0)
                sb.append(sep);
            sb.append(children.get(i));
        }
        sb.append(suffix);
        return sb.toString();
    }
}
