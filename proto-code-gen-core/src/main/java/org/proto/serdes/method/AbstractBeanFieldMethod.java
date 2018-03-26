package org.proto.serdes.method;

import java.lang.reflect.Method;

abstract class AbstractBeanFieldMethod {
    final Method method;

    AbstractBeanFieldMethod(Method method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return method.toString();
    }
}
