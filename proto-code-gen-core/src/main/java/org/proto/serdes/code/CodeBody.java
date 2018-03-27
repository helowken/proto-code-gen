package org.proto.serdes.code;

import org.proto.serdes.utils.Block;
import org.proto.serdes.utils.Element;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CodeBody<T extends Code> extends AbstractCode {
    final List<T> statements = new ArrayList<>();

    CodeBody(Code parent) {
       this.setParent(parent);
    }

    public CodeBody add(T statement) {
        statements.add(statement);
        statement.setParent(this);
        return this;
    }

    public boolean isEmpty() {
        return statements.isEmpty();
    }

    public int size() {
        return statements.size();
    }

    Optional<T> find(Predicate<T> func) {
        for (T stat : statements) {
            if (func.test(stat))
                return Optional.of(stat);
        }
        return Optional.empty();
    }

    boolean exists(Predicate<T> func) {
        return find(func).isPresent();
    }

    CodeBody<T> sort(Comparator<T> comparator) {
        statements.sort(comparator);
        return this;
    }

    Block newBlock() {
        return new Block();
    }

    @Override
    public Element getContent() {
        return newBlock().process(block -> statements.forEach(block::add));
    }

    @Override
    public List<Code> getChildren() {
        return new ArrayList<>(statements);
    }
}
