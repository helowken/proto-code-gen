package org.proto.serdes.file;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

public interface FieldSymbol {
    int getLevel();

    String getTypeString();

    void setActualArgTypes(Type[] argTypes);

    default Set<Message> getInnerMessages() {
        return Collections.emptySet();
    }
}
