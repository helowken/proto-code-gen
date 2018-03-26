package org.proto.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.proto.serdes.ProtoUtils;
import org.proto.utils.CheckUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class BaseGenMojo<T extends BaseConfig> extends AbstractMojo {
    private static final String SUFFIX = ".class";
    private static final int SUFFIX_LEN = SUFFIX.length();

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().debug("Start to generate files...");
        List<T> configs = getConfigs();
        CheckUtils.assertNotEmpty(configs, "Configs can't be empty.");
        for (T config : configs) {
            config.init();
            Set<Class<?>> pojoClasses = new HashSet<>();
            final ClassLoader originClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                traverseDir(config, pojoClasses, originClassLoader);
                getLog().debug("Pojo classes: " + pojoClasses);
                if (!pojoClasses.isEmpty()) {
                    try {
                        generate(config, pojoClasses);
                    } catch (IOException e) {
                        getLog().error(e);
                        throw new MojoExecutionException("Generate file failed, class base dir is: " + config.getBaseDir());
                    }
                }
            } finally {
                Thread.currentThread().setContextClassLoader(originClassLoader);
            }
        }
    }

    private void traverseDir(T config, Set<Class<?>> pojoClasses, ClassLoader originClassLoader) throws MojoFailureException, MojoExecutionException {
        File baseDir = new File(config.getBaseDir());
        String baseDirPath = baseDir.getAbsolutePath();
        final int dirNameLen = baseDirPath.length();
        getLog().debug("Traverse dir: " + baseDirPath);
        File[] files = baseDir.listFiles();
        if (files == null) {
            getLog().debug("No files.");
            return;
        }
        try {
            final URLClassLoader classLoader = new URLClassLoader(
                    new URL[]{
                            baseDir.toURI().toURL()
                    },
                    originClassLoader
            );
            Thread.currentThread().setContextClassLoader(classLoader);
            getLog().debug("===========" + classLoader.loadClass("org.proto.query.ProtoTypeCast").getName());
            for (File file : files)
                access(file, dirNameLen, config, pojoClasses);
        } catch (Exception e) {
            getLog().error(e);
            throw new MojoExecutionException("Traverse class base dir fail: " + baseDirPath);
        }
    }

    private void access(File fileOrDir, int dirNameLen, T config, Set<Class<?>> pojoClasses) throws Exception {
        if (fileOrDir.isFile()) {
            if (fileOrDir.getName().endsWith(SUFFIX)) {
                String fullPath = fileOrDir.getAbsolutePath();
                String classFileName = fullPath.substring(dirNameLen, fullPath.length() - SUFFIX_LEN);
                char c = classFileName.charAt(0);
                if (c == '/' || c == '\\') {
                    classFileName = classFileName.substring(1);
                }
                String className = classFileName.replace("/", ".").replace("\\", ".");
                boolean accepted = config.accept(className);
                getLog().debug("Check accepted: " + className + ": " + accepted);
                if (accepted) {
                    Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
                    boolean hasProtoAnnt = ProtoUtils.hasProtoClassAnnt(clazz);
                    getLog().debug("Check has proto annotation: " + clazz + ", " + hasProtoAnnt);
                    if (hasProtoAnnt)
                        pojoClasses.add(clazz);
                }
            }
        } else {
            File[] files = fileOrDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    access(file, dirNameLen, config, pojoClasses);
                }
            }
        }
    }

    abstract List<T> getConfigs();

    abstract void generate(T config, Collection<Class<?>> pojoClasses) throws IOException;

}
