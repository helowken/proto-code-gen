package org.proto.plugin;

import org.apache.maven.plugins.annotations.Parameter;

public class CodeGenConfig extends BaseConfig {
    @Parameter(required = true)
    String converterClass;
    @Parameter(required = true)
    String outputDir;

    public String getOutputDir() {
        return outputDir;
    }

    public String getConverterClass() {
        return converterClass;
    }
}
