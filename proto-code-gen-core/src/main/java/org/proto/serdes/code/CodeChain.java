package org.proto.serdes.code;

import org.proto.serdes.utils.Block;
import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.Row;

import java.util.List;

public class CodeChain extends CompoundCode<Code, CodeChain> {
    private final boolean multiRows;

    public CodeChain() {
        this(true);
    }

    public CodeChain(boolean multiRows) {
        this.multiRows = multiRows;
    }

    @Override
    public Element getContent() {
        if (multiRows) {
            Block block = new Block();
            List<Code> statements = body.statements;
            if (!statements.isEmpty()) {
                block.add(statements.get(0));
                Block childBlock = new Block();
                block.addChild(childBlock);
                statements.subList(1, statements.size()).forEach(childBlock::addChild);
            }
            return block;
        }
        return new Row().process(row -> body.statements.forEach(row::add));
    }
}
