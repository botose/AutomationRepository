package com.automation.server.feature;


import java.util.List;

public class ScenarioFile {
    private String title;
    private List<String> description;
    private List<Scenario> scenarios;
    private String fileName;

    public ScenarioFile(String fileName) {
        this.fileName = fileName;
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
