package org.proto.plugin;

import org.apache.maven.plugins.annotations.Parameter;

public class CodeGenConfig extends BaseConfig {
    @Parameter(required = true)
    private String converterClass;
    @Parameter(required = true)
    private String outputDir;

    public String getOutputDir() {
        return outputDir;
    }

    public String getConverterClass() {
        return converterClass;
    }
}
