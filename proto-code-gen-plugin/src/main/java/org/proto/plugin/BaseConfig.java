package org.proto.plugin;

import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class BaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(BaseConfig.class);

    @Parameter(required = true)
    String baseDir;
    @Parameter(alias = "includes")
    Set<String> includeStrs;
    @Parameter(alias = "excludes")
    Set<String> excludeStrs;

    private List<Pattern> includePatterns = new ArrayList<>();
    private List<Pattern> excludePatterns = new ArrayList<>();

    String getBaseDir() {
        return baseDir;
    }

    public Set<String> getIncludes() {
        return includeStrs;
    }

    public void setIncludes(Set<String> includeStrs) {
        this.includeStrs = includeStrs;
    }

    public Set<String> getExcludes() {
        return excludeStrs;
    }

    public void setExcludes(Set<String> excludeStrs) {
        this.excludeStrs = excludeStrs;
    }

    void init() {
        if (includeStrs != null)
            includeStrs.forEach(s -> {
                logger.debug("Include expr: " + s);
                includePatterns.add(Pattern.compile(s));
            });
        if (excludeStrs != null)
            excludeStrs.forEach(s -> {
                logger.debug("Exclude expr: " + s);
                excludePatterns.add(Pattern.compile(s));
            });
    }


    boolean accept(String className) {
        for (Pattern pattern : excludePatterns) {
            if (pattern.matcher(className).matches())
                return false;
        }
        for (Pattern pattern : includePatterns) {
            if (pattern.matcher(className).matches())
                return true;
        }
        return includePatterns.isEmpty();
    }

}
