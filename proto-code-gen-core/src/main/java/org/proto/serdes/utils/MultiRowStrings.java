package org.proto.serdes.utils;

public class MultiRowStrings extends AbstractRichString<MultiRowStrings> {
    private boolean includeLastSuffix;

    public MultiRowStrings(String suffix) {
        this("", suffix, true);
    }

    public MultiRowStrings(String prefix, String suffix, boolean includeLastSuffix) {
        super(prefix, suffix);
        this.includeLastSuffix = includeLastSuffix;
    }

    public MultiRowStrings newChildBlock(String prefix, String suffix, boolean includeLastSuffix) {
        MultiRowStrings child = new MultiRowStrings(prefix, suffix, includeLastSuffix);
        this.add(child);
        return child;
    }

    public MultiRowStrings newChildBlock() {
        return newChildBlock(prefix, suffix, includeLastSuffix);
    }

    private void construct(StringBuilder sb, String parentPrefix) {
        final String header = parentPrefix + prefix;
        for (int i = 0, len = children.size(); i < len; ++i) {
            Object row = children.get(i);
            if (row instanceof MultiRowStrings) {
                ((MultiRowStrings) row).construct(sb, header);
            } else {
                sb.append(header);
                sb.append(row);
            }
            if (includeLastSuffix || i < len - 1) {
                sb.append(suffix);
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        construct(sb, "");
        return sb.toString();
    }
}
