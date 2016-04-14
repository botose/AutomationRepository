package com.automation.server.model;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ScenarioFile {
    private List<String> headline;
    private List<Scenario> scenarios;
    private String filePath;
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public ScenarioFile(String filePath) {

    }

    public void readFile() {

    }

    public void writeFile() {

    }

    public List<String> getHeadline() {
        return headline;
    }

    public void setHeadline(List<String> headline) {
        this.headline = headline;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios;
    }
}
