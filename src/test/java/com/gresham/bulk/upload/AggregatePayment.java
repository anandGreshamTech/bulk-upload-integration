package com.gresham.bulk.upload;

import com.gresham.bulk.upload.service.KubeCommands;
import com.gresham.bulk.upload.service.Loader;
import com.gresham.bulk.upload.service.ResourceReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
class AggregatePayment {

    static String dataDir = "src/test/resources/bulkUpload/aggPayment";
    
    @Autowired
    private KubeCommands kubeCommands;
    @Autowired
    private ResourceReader reader;
    @Autowired
    private Loader loader;
    static boolean devMode=true;
    static String authlink = "SMOKESORA";
    static String type = "AggPay";
    static List<Path> aggregatePaymentTestFiles() {
        List<Path> tempDir = ResourceReader.getTempDir(dataDir);
        List<Path> result = new LinkedList<>();
        String drRegex = devMode ? "headerCLA&ACCInfoBothPresent" : ".*";
        Pattern pattern = Pattern.compile(drRegex);
        tempDir.forEach(path -> {
            if (pattern.matcher(path.getFileName().toString()).find()) {
                result.add(path);
            }
        });
        return result;
    }

    @ParameterizedTest
    @MethodSource("aggregatePaymentTestFiles")
    void aggPayments(Path path) {
        List<String> actual = new LinkedList<>();
        List<String> expected;
            log.info("In progress {" + path.getFileName() + "}");
            List<Path> files = reader.getFiles(path);
            Path testFile = reader.createTestFile(reader.getInputFile(files, "data"), authlink, type);
            log.info(testFile.toString());
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
            log.info("{expected}\n :" + expected);
            log.info("----------------");
            log.info("{actual}\n :" + actual);

            assertTrue(CollectionUtils.isEqualCollection(expected, actual));
            reader.cleanUp(reader.getFiles(path), "Pay_");
        }

}
