package org.proto.plugin;

import org.apache.maven.plugins.annotations.Parameter;

import java.util.concurrent.atomic.AtomicInteger;

public class ProtoFileGenConfig extends BaseConfig {
    private static final AtomicInteger id = new AtomicInteger(0);
    private static final String defaultPackage = "proto.code.gen";
    private static final String defaultClassPrefix = defaultPackage + "." + "DefaultProtoOuter";

    @Parameter(required = true)
    private String outputFile;
    @Parameter
    private String protoClass;

    @Override
    protected void init() {
        if (protoClass == null)
            protoClass = defaultClassPrefix + id.getAndAdd(1);
        if (!protoClass.contains("."))
            protoClass = defaultPackage + "." + protoClass;
        super.init();
    }

    String getOutputFile() {
        return outputFile;
    }

    String getProtoClass() {
        return protoClass;
    }

    @Override
    public String toString() {
        return "ProtoFileGenConfig{" +
                "baseDir='" + baseDir + '\'' +
                ", outputFile='" + outputFile + '\'' +
                ", protoClass='" + protoClass + '\'' +
                ", includeStrs=" + includeStrs +
                ", excludeStrs=" + excludeStrs +
                '}';
    }
}
