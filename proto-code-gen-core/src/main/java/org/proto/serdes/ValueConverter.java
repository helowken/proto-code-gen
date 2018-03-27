package org.proto.serdes;

import org.proto.serdes.transform.TransformMgr;
import org.proto.serdes.type.TypeClass;

interface ValueConverter {
    Object apply(TypeClass valueType, Object oldValue, ConvertFunc convertFunc, TransformMgr.FuncCache funcCache);
}
