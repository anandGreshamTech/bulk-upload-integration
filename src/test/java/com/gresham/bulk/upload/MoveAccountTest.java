package com.gresham.bulk.upload;

import com.gresham.bulk.upload.service.KubeCommands;
import com.gresham.bulk.upload.service.Loader;
import com.gresham.bulk.upload.service.ResourceReader;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MoveAccountTest {

    static String dataDir = "src/test/resources/bulkUpload/moveAccount";

    @Autowired
    private KubeCommands kubeCommands;
    @Autowired
    private ResourceReader reader;
    @Autowired
    private Loader loader;

    static String authlink = "SMOKESORA";
    static String type = "Move";

    @Test
    public void moveAccount() {
        List<String> actual = new LinkedList<>();
        List<String> expected = new LinkedList<>();
        
        String drRegex = "(sc\\d+-)|(-sc\\d+)";
        Pattern pattern = Pattern.compile(drRegex);
        List<Path> accountOpenTestScenarios = reader.getDirs(dataDir);
        for (Path path : accountOpenTestScenarios) {
            if (pattern.matcher(path.getFileName().toString()).find()) {
                System.out.println("In progress {" + path.getFileName() + "}");
                List<Path> files = reader.getFiles(path);
                Path testFile = reader.createTestFile(reader.getInputFile(files, "data"), authlink, type);
                System.out.println(testFile.toString());
                Path expectedFile = reader.getInputFile(files, "expected");
                List<String> commsOut = loader.run(kubeCommands.getCommsOutPodCommand(), false);
                loader.run(kubeCommands.getCopyTestFile(testFile), false);
                String rejectFile = testFile.getFileName().toString().replace(".csv", "_REJECT.csv");
                String responseFile = testFile.getFileName().toString().replace(".csv", "_RESPONSE.csv");
                String[] readFileFromConsole = kubeCommands.readFileFromConsole(commsOut.get(0), rejectFile);
                if (expectedFile.getFileName().toString().toUpperCase().contains("RESPONSE")) {
                    readFileFromConsole = kubeCommands.readFileFromConsole(commsOut.get(0), responseFile);
                    if (reader.isFileCreated(commsOut.get(0), responseFile)) {
                        actual = loader.run(readFileFromConsole, true);
                    }
                } else {

                    if (reader.isFileCreated(commsOut.get(0), rejectFile)) {
                        actual = loader.run(readFileFromConsole, true);
                    }

                }
                try {
                    expected = Files.readAllLines(expectedFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("{expected}\n :" + expected);
                System.out.println("----------------");
                System.out.println("{actual}\n :" + actual);

                assertTrue(CollectionUtils.isEqualCollection(expected, actual));
                reader.cleanUp(reader.getFiles(path), "Move_");
            }
        }
    }

}
