package org.proto.serdes.info;

public class ProtoClassInfo {
    private final String protoClass;

    public ProtoClassInfo(String protoClass) {
        if (protoClass == null || protoClass.isEmpty())
            throw new IllegalArgumentException("Proto class name can not be null or empty.");
        this.protoClass = protoClass;
    }

    public String getProtoClass() {
        return protoClass;
    }
}
