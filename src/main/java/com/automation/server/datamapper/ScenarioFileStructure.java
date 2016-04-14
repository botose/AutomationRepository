package com.automation.server.datamapper;

import java.util.regex.Pattern;

public interface ScenarioFileStructure {
    Pattern getTitlePattern();
    Pattern getStepPattern();
    Pattern getAnnotationPattern();
}
