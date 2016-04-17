package com.automation.server.datamapper;


import com.automation.server.model.Scenario;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileDataMapper {
    public static List<Scenario> mapFileToScenarios(String filePath, Charset charset, ScenarioFileStructure structure) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), charset)) {
            String line = null;
            int stage = 0;
            while ((line = reader.readLine()) != null) {
                switch(stage) {
                    case 0:
                        if (structure.getTitlePattern().matcher(line.trim()).lookingAt()) {
                            stage++;
                        } else {

                        }
                        break;
                    case 1:
                        if (structure.get().matcher(line.trim()).lookingAt()) {
                            stage++;
                        } else {

                        }
                        break;
                    case 2:
                        if (structure.getTitlePattern().matcher(line.trim()).lookingAt()) {
                            stage--;
                        } else {

                        }
                        break;
                }
                line = line.trim();
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        List<Scenario> scenarios = new ArrayList<>();

        return scenarios;
    }
}
