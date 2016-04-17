package com.automation.server.model;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ScenarioFile {
    private String title;
    private List<String> description;
    private List<Scenario> scenarios;
    private String filePath;
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public ScenarioFile(String filePath) {

    }

    public void readFile() {

    }

    public void writeFile() {

    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
