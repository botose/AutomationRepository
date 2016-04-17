package com.automation.server.feature;

import com.automation.server.feature.datamapper.SimpleFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.file.Path;

@Component("ScenarioFileBuilder")
public class ScenarioFileBuilder {

    @Autowired
    private SimpleFileMapper simpleFileMapper;

    public ScenarioFile build(Path path, Charset charset) {

        return simpleFileMapper.map(path, charset);
    }
}
