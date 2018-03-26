package org.proto.serdes.method;

import org.proto.serdes.code.Code;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;

abstract class AbstractDirectFieldMethod {
    final Field field;

    AbstractDirectFieldMethod(Field field) {
        this.field = field;
    }

    boolean isPublic() {
        return Modifier.isPublic(field.getModifiers());
    }

    Object apply(ApplyFunc applyFunc) throws Exception {
        if (isPublic())
            return applyFunc.apply();
        boolean accessible = field.isAccessible();
        try {
            return applyFunc.apply();
        } finally {
            field.setAccessible(accessible);
        }
    }

    Code genCodeHelper(Supplier<Code> codeSupplier) {
        if (isPublic())
            return codeSupplier.get();
        throw new UnsupportedOperationException(field + " is not accessible to generate code.");
    }

    @Override
    public String toString() {
        return "direct access => " + field;
    }

    interface ApplyFunc {
        Object apply() throws Exception;
    }
}
