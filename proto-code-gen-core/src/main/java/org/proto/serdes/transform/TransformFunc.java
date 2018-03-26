package org.proto.serdes.transform;

import org.proto.serdes.code.Code;

public interface TransformFunc {
    Object transform(Object oldValue);

    Code genCode(Code inputCode, boolean boxed);
}
