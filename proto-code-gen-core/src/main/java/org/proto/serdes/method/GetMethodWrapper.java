package org.proto.serdes.method;

import org.proto.serdes.code.Code;

public interface GetMethodWrapper {
    Object getValue(Object caller) throws Exception;

    Code genCode(Code caller);
}
