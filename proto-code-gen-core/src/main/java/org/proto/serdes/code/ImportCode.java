package org.proto.serdes.code;

import org.proto.serdes.utils.Element;
import org.proto.serdes.utils.Row;

public class ImportCode extends AbstractCode<ImportCode> {
    private final Class<?> clazz;

    public ImportCode(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getImportClass() {
        return clazz;
    }

    public String getImportClassName() {
        return clazz.getName();
    }

    @Override
    public Element getContent() {
        return new Row().add("import " + getImportClassName() + ";");
    }
}
