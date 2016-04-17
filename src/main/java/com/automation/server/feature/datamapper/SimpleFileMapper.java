package com.automation.server.feature.datamapper;


import com.automation.server.feature.Scenario;
import com.automation.server.feature.ScenarioFile;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component("SimpleFileMapper")
public class SimpleFileMapper implements FileMapper {
    public ScenarioFile map(Path filePath, Charset charset) {
        ScenarioFile scenarioFile = new ScenarioFile(filePath.getFileName().toString());
        try (BufferedReader reader = Files.newBufferedReader(filePath, charset)) {
            String line;
            int stage = 0;
            Scenario scenario = new Scenario();
            List<String> lines = new ArrayList<>();
            List<Scenario> scenarios = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                switch(stage) {
                    case 0:
                        scenarioFile.setTitle(line);
                        stage++;
                        break;
                    case 1:
                        if (!line.equals("")) {
                            lines.add(line);
                        } else {
                            scenarioFile.setDescription(new ArrayList<>(lines));
                            lines.clear();
                            stage++;
                        }
                        break;
                    case 2:
                        if(line.contains("Scenario Outline: ")) {
                            scenario.setTitle(line);
                            stage++;
                            break;
                        }
                        stage++;
                    case 3:
                        if (!line.equals("")) {
                            lines.add(line);
                        } else {
                            scenario.setContent(new ArrayList<>(lines));
                            lines.clear();
                            scenarios.add(scenario);
                            scenario = new Scenario();
                            stage = 2;
                        }
                        break;
                    default:
                        break;
                }
            }
            scenarioFile.setScenarios(scenarios);
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

        return scenarioFile;
    }
}
