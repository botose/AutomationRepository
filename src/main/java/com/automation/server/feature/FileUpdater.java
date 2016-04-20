package com.automation.server.feature;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component("FileUpdater")
public class FileUpdater {

    public static final String NEW_LINE = "\r\n";

    public void updateFile(String path, FeatureFile feature, Charset charset) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path), charset))
        {
            writeLine(feature.getTitle(), writer);
            writeLines(feature.getDescription(), writer);
            writeEmptyLine(writer);

            for(Scenario scenario : feature.getScenarios()) {
                writeLine(scenario.getTitle(), writer);
                writeLines(scenario.getContent(), writer);
                writeEmptyLine(writer);
            }

        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

    private void writeEmptyLine(BufferedWriter writer) throws IOException {
        writer.newLine();
    }

    private void writeLine(String line, BufferedWriter writer) throws IOException {
        if(!line.equals("")) {
            writer.write(line, 0, line.length());
            writer.newLine();
        }
    }

    private void writeLines(List<String> lines, BufferedWriter writer) throws IOException {
        if(lines != null) {
            for (String line : lines) {
                writeLine(line, writer);
            }
        }
    }

}
