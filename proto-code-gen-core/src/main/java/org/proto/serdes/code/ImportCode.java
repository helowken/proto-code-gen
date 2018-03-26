package org.proto.serdes.code;

import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.Row;

public class ImportCode extends AbstractCode<ImportCode> {
    private final String className;

    public ImportCode(String className) {
        this.className = className;
    }

    String getClassName() {
        return className;
    }

    @Override
    public Element getContent() {
        return new Row().add("import " + className + ";");
    }
}
