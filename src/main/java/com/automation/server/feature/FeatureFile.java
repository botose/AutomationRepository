package com.automation.server.feature;


import org.springframework.hateoas.ResourceSupport;

import java.util.List;

public class FeatureFile {
    private String title;
    private String fileName;
    private List<String> description;
    private List<Scenario> scenarios;

    public FeatureFile(String fileName) {
        this.fileName = fileName;
    }

    public FeatureFile() {}

    public String getFileName() {
        return fileName;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getDescription() {
        return description;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setScenario(String scenarioTitle, Scenario scenario) {
        for(int i = 0 ; i < scenarios.size() ; ++i) {
            if(scenarios.get(i).getTitle().contains(scenarioTitle)) {
                scenarios.add(i, scenario);
                scenarios.remove(i+1);
            }
        }
    }

    public void removeScenario(String scenarioTitle) {
        for(int i = 0 ; i < scenarios.size() ; ++i) {
            if(scenarios.get(i).getTitle().contains(scenarioTitle)) {
                scenarios.remove(i);
            }
        }
    }
}
