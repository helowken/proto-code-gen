package org.proto.serdes;

interface ValueConverter {
    Object apply(CodecField codecField, Object oldValue, ConvertFunc convertFunc) throws Exception;
}
