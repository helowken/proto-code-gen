package org.proto.serdes.method;

import org.proto.serdes.code.Code;

public interface SetMethodWrapper {
    void setValue(Object caller, Object v) throws Exception;

    Code genCode(Code caller, Code value);
}
