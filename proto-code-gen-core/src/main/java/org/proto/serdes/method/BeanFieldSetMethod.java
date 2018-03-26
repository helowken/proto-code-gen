package org.proto.serdes.method;

import org.proto.serdes.code.Code;
import org.proto.serdes.code.MethodCallCode;

import java.lang.reflect.Method;

public class BeanFieldSetMethod extends AbstractBeanFieldMethod implements SetMethodWrapper {

    public BeanFieldSetMethod(Method method) {
        super(method);
    }

    @Override
    public void setValue(Object caller, Object v) throws Exception {
        method.invoke(caller, v);
    }

    @Override
    public Code genCode(Code caller, Code value) {
        return new MethodCallCode(caller, method.getName(), value);
    }

}
