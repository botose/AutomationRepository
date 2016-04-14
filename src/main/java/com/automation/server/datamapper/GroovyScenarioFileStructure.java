package com.automation.server.datamapper;


import java.util.regex.Pattern;

public class GroovyScenarioFileStructure implements ScenarioFileStructure {

    private static final Pattern TITLE_PATTERN = Pattern.compile("^(?:Scenario Outline:)(?:.*)");
    private static final Pattern ANNOTATION_PATTERN = Pattern.compile("^(?:@)(?:.*)");
    private static final Pattern STEP = Pattern.compile("^[(?:given)(?:and)(?:when)(?:then)(?:examples)(?:|)](?:.*)");

    @Override
    public Pattern getTitlePattern() {
        return TITLE_PATTERN;
    }

    @Override
    public Pattern getStepPattern() {
        return STEP;
    }

    @Override
    public Pattern getAnnotationPattern() {
        return ANNOTATION_PATTERN;
    }
}
