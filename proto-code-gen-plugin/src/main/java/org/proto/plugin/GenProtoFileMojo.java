package org.proto.plugin;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.proto.serdes.file.ProtoFileCreater;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Mojo(name = "gen-proto-file")
public class GenProtoFileMojo extends BaseGenMojo<ProtoFileGenConfig> {
    @Parameter(required = true)
    List<ProtoFileGenConfig> protoFileGenConfigs;

    @Override
    List<ProtoFileGenConfig> getConfigs() {
        return protoFileGenConfigs;
    }

    @Override
    void generate(ProtoFileGenConfig config, Collection<Class<?>> pojoClasses) throws IOException {
        File outputFile = ProtoFileCreater.createProtoFile(
                config.getProtoClass(),
                config.getOutputFile(),
                pojoClasses.toArray(new Class[0])
        );
        getLog().info("Proto file created: " + outputFile.getAbsolutePath());
    }

}
