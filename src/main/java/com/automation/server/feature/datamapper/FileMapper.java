package com.automation.server.feature.datamapper;

import com.automation.server.feature.ScenarioFile;

import java.nio.charset.Charset;
import java.nio.file.Path;

public interface FileMapper {
    ScenarioFile map(Path filePath, Charset charset);
}
