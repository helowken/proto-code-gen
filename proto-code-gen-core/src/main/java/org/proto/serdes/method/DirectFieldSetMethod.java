package org.proto.serdes.method;

import org.proto.serdes.code.AssignmentCode;
import org.proto.serdes.code.Code;
import org.proto.serdes.code.RefCode;
import org.proto.serdes.code.VariableCode;

import java.lang.reflect.Field;

public class DirectFieldSetMethod extends AbstractDirectFieldMethod implements SetMethodWrapper {
    public DirectFieldSetMethod(Field field) {
        super(field);
    }

    @Override
    public void setValue(Object caller, Object v) throws Exception {
        apply(() -> {
            field.set(caller, v);
            return null;
        });
    }

    @Override
    public Code genCode(Code caller, Code value) {
        return genCodeHelper(() -> new AssignmentCode<>(new RefCode(caller, new VariableCode(field.getName())), value));
    }

}
