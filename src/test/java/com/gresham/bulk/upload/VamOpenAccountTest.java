package com.gresham.bulk.upload;

import com.gresham.bulk.upload.service.KubeCommands;
import com.gresham.bulk.upload.service.Loader;
import com.gresham.bulk.upload.service.ResourceReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Slf4j
class VamOpenAccountTest {

    static String closeAccountDataDir = "src/test/resources/bulkUpload/vamOpenAccount";
    static String resultDir = "/home/ccmsim/bulkupload/response";
    static String container = "simulator-sftp-server";

    @Autowired
    private KubeCommands kubeCommands;
    @Autowired
    private ResourceReader reader;
    @Autowired
    private Loader loader;
    static boolean devMode = true;

    static List<Path> vamOpenAccountData() {
        String drRegex = devMode ? "^(sc-.*|.*-sc)$" : ".*";
        Pattern pattern = Pattern.compile(drRegex);
        List<Path> result = new LinkedList<>();
        List<Path> testFileFolder = ResourceReader.getTempDir(closeAccountDataDir);
        testFileFolder.forEach(path -> {
                    if (pattern.matcher(path.getFileName().toString()).find()) {
                        result.add(path);
                    }
                }
        );
        return result;
    }

    @ParameterizedTest
    @MethodSource("vamOpenAccountData")
    void testCloseAccount(Path path) throws IOException {

        List<String> actual = new LinkedList<>();
        List<String> expected;
        log.info("In progress {" + path.getFileName() + "}");
        List<Path> files = reader.getFiles(path);
        Path testFile = createTestFile(reader.getInputFile(files, "data"), "Close");
        log.info(testFile.toString());
        Path expectedFile = reader.getInputFile(files, "expected");
        List<String> simulator = loader.run(kubeCommands.getSimulatorPodCommand(), false);
        loader.run(kubeCommands.getCopyTestFile(testFile), false);
        String rejectFile = testFile.getFileName().toString().replace(".csv", "_REJECT.csv");
        String responseFile = testFile.getFileName().toString().replace(".csv", "_RESPONSE.csv");
        String[] readFileFromConsole = kubeCommands.readFileFromConsole(simulator.get(0), container, rejectFile, resultDir);
        boolean actualFileFound = false;
        if (expectedFile.getFileName().toString().toUpperCase().contains("RESPONSE")) {
            readFileFromConsole = kubeCommands.readFileFromConsole(simulator.get(0), container, responseFile, resultDir);
            if (kubeCommands.isFileCreated(simulator.get(0),container, resultDir, responseFile)) {
                actualFileFound = true;
                actual = loader.run(readFileFromConsole, true);
            }
        } else {
            if (kubeCommands.isFileCreated(simulator.get(0),container, resultDir, rejectFile)) {
                actualFileFound = true;
                Arrays.stream(readFileFromConsole)
                        .forEach(System.out::print);
                actual = loader.run(readFileFromConsole, true);
            }

        }
        try {
            expected = Files.readAllLines(expectedFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (actualFileFound) {

            log.info("{expected}\n :" + expected);
            log.info("----------------");
            log.info("{actual}\n :" + actual);

            assertTrue(CollectionUtils.isEqualCollection(expected, actual));
        } else {
            log.info("{ACTUAL FILE NOT FOUND}");
            fail();
        }
        reader.cleanUp(reader.getFiles(path), "Close_");

    }

    public Path createTestFile(Path scenarioFile,  String fileNamePrefixType) throws IOException {
        String authLinkLine= Files.lines(scenarioFile).skip(1).findFirst().toString();
        String authLink = Arrays.asList(authLinkLine.split(",")).get(2);
        Path target = Path.of(scenarioFile.getParent().toString().concat("/" + reader.createFileName(authLink, fileNamePrefixType)));
        try {
            Files.copy(scenarioFile, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return target;
    }
}
