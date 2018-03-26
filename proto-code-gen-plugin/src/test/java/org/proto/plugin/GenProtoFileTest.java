package org.proto.plugin;

import java.lang.reflect.Field;
import java.util.Collections;

public class GenProtoFileTest {

    public static void main(String[] args) throws Exception {
        GenProtoFileMojo mojo = new GenProtoFileMojo();

        ProtoFileGenConfig config = new ProtoFileGenConfig();
        setFieldValue(config, "baseDir", "C:\\Users\\EZLINMI\\projects\\proto-code-gen\\proto-code-gen-test\\target\\classes");
        setFieldValue(config, "outputFile", "C:\\Users\\EZLINMI\\projects\\proto-code-gen\\proto-code-gen-test\\src\\main\\resources\\proto\\test.proto");
        setFieldValue(config, "includeStrs", Collections.singleton("org\\.proto\\.test\\.entity\\.\\w*"));

        mojo.protoFileGenConfigs = Collections.singletonList(config);
        mojo.execute();
    }

    private static void setFieldValue(Object source, String fieldName, Object value) throws Exception {
        Field field = source.getClass().getDeclaredField(fieldName);
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(source, value);
        } finally {
            field.setAccessible(accessible);
        }
    }
}
