package org.proto.serdes.method;

import org.proto.serdes.code.Code;
import org.proto.serdes.code.MethodCallCode;

import java.lang.reflect.Method;

public class BeanFieldGetMethod extends AbstractBeanFieldMethod implements GetMethodWrapper {

    public BeanFieldGetMethod(Method method) {
        super(method);
    }

    @Override
    public Object getValue(Object caller) throws Exception {
        return method.invoke(caller);
    }

    @Override
    public Code genCode(Code caller) {
        return new MethodCallCode(caller, method.getName());
    }

}
