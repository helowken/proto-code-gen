package org.proto.serdes.method;

import org.proto.serdes.code.Code;
import org.proto.serdes.code.RefCode;
import org.proto.serdes.code.VariableCode;

import java.lang.reflect.Field;

public class DirectFieldGetMethod extends AbstractDirectFieldMethod implements GetMethodWrapper {

    public DirectFieldGetMethod(Field field) {
        super(field);
    }

    @Override
    public Object getValue(Object caller) throws Exception {
        return apply(() -> field.get(caller));
    }

    @Override
    public Code genCode(Code caller) {
        return genCodeHelper(() -> new RefCode(caller, new VariableCode(field.getName())));
    }

}
