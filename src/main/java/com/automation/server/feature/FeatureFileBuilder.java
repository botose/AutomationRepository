package com.automation.server.feature;

import com.automation.server.feature.datamapper.SimpleFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.file.Path;

@Component("FeatureFileBuilder")
public class FeatureFileBuilder {

    @Autowired
    private SimpleFileMapper simpleFileMapper;

    public FeatureFile build(Path path, Charset charset) {

        return simpleFileMapper.map(path, charset);
    }
}
