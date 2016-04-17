package com.automation.server.feature.datamapper;

import com.automation.server.feature.FeatureFile;

import java.nio.charset.Charset;
import java.nio.file.Path;

public interface FileMapper {
    FeatureFile map(Path filePath, Charset charset);
}
