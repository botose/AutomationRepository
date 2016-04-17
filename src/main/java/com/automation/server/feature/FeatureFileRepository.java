package com.automation.server.feature;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Component("FeatureFileRepository")
public class FeatureFileRepository {
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private static Map<String, FeatureFile> files;
    private static String rootPath;

    @Autowired
    FeatureFileBuilder featureFileBuilder;

    @Autowired
    FileUpdater fileUpdater;

    public void loadFiles(String path) throws IOException {
        rootPath = path;
        files = new HashMap<>();
        Files.walk(Paths.get(path)).forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                files.put(filePath.toString(), featureFileBuilder.build(filePath, CHARSET));
            }
        });
    }

    public boolean hasFiles() {
        return files == null || files.size() > 0;
    }

    public Map<String, FeatureFile> getFiles(){
        return files;
    }

    public void updateFile(String filePath) {
        fileUpdater.updateFile(rootPath + "/" + filePath, files.get(rootPath + "/" + filePath), CHARSET);
    }

    public FeatureFile getFile(String name) throws NoSuchFileException {
        return files.get(rootPath + "/" + name);
    }

    public void setFile(String fileName, FeatureFile featureFile) {
        files.put(rootPath + "/" + fileName, featureFile);
    }

}
