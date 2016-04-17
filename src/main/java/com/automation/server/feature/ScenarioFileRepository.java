package com.automation.server.feature;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Component("ScenarioFileRepository")
public class ScenarioFileRepository {
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private static Map<String, ScenarioFile> files;

    @Autowired
    ScenarioFileBuilder scenarioFileBuilder;

    @Autowired
    FileUpdater fileUpdater;

    public void loadFiles(String path) throws IOException {
        files = new HashMap<>();
        Files.walk(Paths.get(path)).forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                files.put(filePath.toString(), scenarioFileBuilder.build(filePath, CHARSET));
            }
        });
    }

    public boolean hasFiles() {
        return files.size()>0;
    }

    public Map<String, ScenarioFile> getFiles(){
        return files;
    }

    public void updateFile(String filePath) {
        fileUpdater.updateFile(filePath, files.get(filePath), CHARSET);
    }

}
