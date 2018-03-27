package org.proto.serdes.code;

import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.Row;

import java.util.Arrays;
import java.util.List;

public class AssignmentCode<T extends Code> extends AbstractCode<AssignmentCode<T>> {
    private final T left;
    private final Code right;

    public AssignmentCode(T left, Code right) {
        this.left = left;
        this.right = right;
        left.setParent(this);
        right.setParent(this);
    }

    public T getLeft() {
        return left;
    }

    public Code getRight() {
        return right;
    }

    @Override
    public List<Code> getChildren() {
        return Arrays.asList(left, right);
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
