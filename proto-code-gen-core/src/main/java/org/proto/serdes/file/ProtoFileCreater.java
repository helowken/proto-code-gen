package org.proto.serdes.file;

import org.proto.serdes.ProtoCodeGen;
import org.proto.serdes.code.ClassCode;
import org.proto.serdes.utils.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import static org.proto.utils.CheckUtils.assertNotEmpty;

public class ProtoFileCreater {

    private static File writeToFile(String outputFileName, String content) throws IOException {
        File outputFile = new File(outputFileName);
        try (BufferedWriter out = new BufferedWriter(new FileWriter(outputFile))) {
            out.write(content);
        }
        return outputFile;
    }

    private static void checkOutputDir(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists())
            throw new RuntimeException("Output dir does not exists: " + dir.getAbsolutePath());
        if (!dir.isDirectory())
            throw new RuntimeException("Output dir is not a directory: " + dir.getAbsolutePath());
    }

    private static Pair<String, String> getPackageAndClass(String fullClassName) {
        int pos = fullClassName.lastIndexOf(".");
        String packageName = "";
        String className = fullClassName;
        if (pos > -1) {
            packageName = className.substring(0, pos);
            className = className.substring(pos + 1);
        }
        return new Pair<>(packageName, className);
    }

    private static void checkArgs(String className, String outputPath, Class<?>[] pojoClasses) {
        assertNotEmpty(className, "Class name can't be empty.");
        assertNotEmpty(pojoClasses, "Pojo classes can't be empty.");
        assertNotEmpty(outputPath, "Output path can't be empty.");
        checkOutputDir(outputPath);
    }

    public static File createProtoFile(String fullClassName, String outputFileName, Class<?>[] pojoClasses) throws IOException {
        String outputDirPath = new File(outputFileName).getParent();
        checkArgs(fullClassName, outputDirPath, pojoClasses);
        Pair<String, String> p = getPackageAndClass(fullClassName);
        ProtoFile protoFile = new ProtoFile(p.left, p.right, Arrays.asList(pojoClasses));
        return writeToFile(outputFileName, protoFile.getContent());
    }

    public static File createConverter(String fullClassName, String outputDir, Class<?>[] pojoClasses) throws IOException {
        checkArgs(fullClassName, outputDir, pojoClasses);
        File outputFile = new File(outputDir, fullClassName.replace(".", "/") + ".java");
        File packageDir = outputFile.getParentFile();
        if (!packageDir.exists()) {
            if (!packageDir.mkdirs())
                throw new RuntimeException("package dir does not exist and can not be created: " + packageDir.getAbsolutePath());
        }
        Pair<String, String> p = getPackageAndClass(fullClassName);
        ClassCode classCode = ProtoCodeGen.generate(p.left, p.right, pojoClasses);
        return writeToFile(outputFile.getAbsolutePath(), classCode.toString());
    }
}
