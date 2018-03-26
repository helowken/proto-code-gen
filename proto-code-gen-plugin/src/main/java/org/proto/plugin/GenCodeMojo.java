package org.proto.plugin;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.proto.serdes.file.ProtoFileCreater;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Mojo(name = "gen-code")
public class GenCodeMojo extends BaseGenMojo<CodeGenConfig> {

    @Parameter(required = true)
    private List<CodeGenConfig> codeGenConfigs;

    @Override
    List<CodeGenConfig> getConfigs() {
        return codeGenConfigs;
    }

    @Override
    void generate(CodeGenConfig config, Collection<Class<?>> pojoClasses) throws IOException {
        File outputFile = ProtoFileCreater.createConverter(
                config.getConverterClass(),
                config.getOutputDir(),
                pojoClasses.toArray(new Class[0])
        );
        getLog().info("Converter file created: " + outputFile.getAbsolutePath());
    }
}
