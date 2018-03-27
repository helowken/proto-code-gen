package org.proto.plugin;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.proto.serdes.file.ProtoFileCreater;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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

    public static void main(String[] args) throws Exception {
        String baseDir = "C:\\Users\\EZLINMI\\projects\\proto-code-gen\\proto-code-gen-test\\target\\classes";
        String outputDir = "C:\\Users\\EZLINMI\\projects\\proto-code-gen\\proto-code-gen-test\\src\\test\\java";
        String converterClass = "EntityConverter";
        Set<String> includes = Collections.singleton("org\\.proto\\.test\\.entity\\.\\w+");
        CodeGenConfig config = new CodeGenConfig();
        config.baseDir = baseDir;
        config.outputDir = outputDir;
        config.converterClass = converterClass;
        config.includeStrs = includes;

        GenCodeMojo mojo = new GenCodeMojo();
        mojo.codeGenConfigs = Collections.singletonList(config);

        mojo.execute();
    }
}
