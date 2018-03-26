package org.proto.serdes.code;

import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.Row;

public class AssignmentCode<T extends Code> extends AbstractCode<AssignmentCode<T>> {
    private final T left;
    private final Code right;

    public AssignmentCode(T left, Code right) {
        this.left = left;
        this.right = right;
    }

    public T getLeft() {
        return left;
    }

    public Code getRight() {
        return right;
    }

    @Override
    public Element getContent() {
        return new Row()
                .add(left)
                .add(" = ")
                .add(right)
                .add(endfix);
    }
}
