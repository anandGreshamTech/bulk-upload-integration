package com.gresham.bulk.upload.test;

import com.github.javafaker.Faker;
import com.gresham.bulk.upload.BulkUploadTestProcessor;
import com.gresham.bulk.upload.UploadType;
import com.gresham.bulk.upload.service.KubeCommands;
import com.gresham.bulk.upload.service.Loader;
import com.gresham.bulk.upload.service.QueryService;
import com.gresham.bulk.upload.service.ResourceReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Slf4j
class CloseAccountTestV2 implements BulkUploadTestProcessor {
    @Autowired
    private KubeCommands kubeCommands;
    @Autowired
    private ResourceReader reader;
    @Autowired
    private Loader loader;
    @Autowired
    QueryService queryService;


    @ParameterizedTest(name = "{0} test")
    @MethodSource("com.gresham.bulk.upload.dataprovider.CloseAccountDataProvider#headerInvalidScenarios")
    void testHeaderValidations(String scenario, String resultType, List<String> data, List<String> expected) {
        var authLink = findAuthLink(queryService.findCustomersOfType("CLIENT_MONIES"));
        var fileData = new StringBuilder();
        logCustomerDetails(authLink);
        List<String> accounts = queryService.findAccountNumberForClosure(authLink);
        var header = updateNonCustomerCodeCases(scenario, data.get(1), authLink);
        var closeAccountRecord = updateAccountNumberInCloseAccountRecord(data.get(2), accounts);
        closeAccountRecord = UpdateRecipientDetailsInCloseAccountRecord(closeAccountRecord, true, true, true);
        fileData.append(data.get(0)).append("\n").append(header).append("\n").append(closeAccountRecord);
        assertFalse(authLink.isBlank(), "No customer found for this test check conditions in findCustomersOfType");
        createTestFileForHeaderValidationAndCompareResult(authLink, expected, fileData, resultType);
    }


    private String updateNonCustomerCodeCases(String testName, String header, String authLink) {
        if (!testName.equalsIgnoreCase("InvalidCustomerCode")) {
            header = header.replace("${authlink}", authLink);
        }
        return header;
    }

    private String updateAccountNumberInCloseAccountRecord(String closeAccountRecord, List<String> accounts) {
        return closeAccountRecord.replace("${account}", accounts.get(0));
    }

    private String UpdateRecipientDetailsInCloseAccountRecord(String closeAccountRecord, boolean updateBsb, boolean updateRecipientAccount, boolean updateRecipientName) {

        Faker faker = new Faker();
        var bsb = faker.number().digits(6);
        var recipientAccount = faker.number().digits(9);
        var recipientName = faker.name().fullName();
        var updatedRecord = closeAccountRecord;

        updatedRecord = updateBsb ? updatedRecord.replace("${bsb}", bsb) : updatedRecord;
        updatedRecord = updateRecipientAccount ? updatedRecord.replace("${recipientAccount}", recipientAccount) : updatedRecord;
        updatedRecord = updateRecipientName ? updatedRecord.replace("${recipientName}", recipientName) : updatedRecord;

        return updatedRecord;
    }

    private void logCustomerDetails(String authLink) {
        log.info("CUSTOMER USED FOR TEST: {}", authLink);
    }

    private String findAuthLink(List<String> customers) {
        return customers.get(ThreadLocalRandom.current().nextInt(customers.size()));
    }


    private Path createTestFileHeaderCases(String fileNamePrefixType, String authLink, StringBuilder fileData) {
        String tempDir = UploadType.CLOSE_ACCOUNT.getDataDir().concat("/tmp/");
        Path path = Path.of(tempDir + reader.createFileName(authLink, fileNamePrefixType));
        try {
            Files.writeString(path, fileData.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return path;
    }

    void createTestFileForHeaderValidationAndCompareResult(String authLink, List<String> expected, StringBuilder fileData, String resultType) {
        assertFalse(authLink.isBlank(), "No customer found for this test check conditions in findCustomersOfType");
        List<String> actual;
        Path testFile = createTestFileHeaderCases(UploadType.CLOSE_ACCOUNT.getFilePrefix(), authLink, fileData);
        List<String> simulator = loader.run(kubeCommands.getSimulatorPodCommand(), false);
        loader.run(kubeCommands.getCopyTestFile(testFile), false);
        String expectedFileName = testFile.getFileName().toString().replace(".csv", "_REJECT.csv");
        if (resultType.equalsIgnoreCase("RESPONSE")) {
            expectedFileName = testFile.getFileName().toString().replace(".csv", "_RESPONSE.csv");
        }
        String[] readFileFromConsole = kubeCommands.readFileFromConsole(simulator.get(0), CONTAINER, expectedFileName, RESULT_DIR);

        if (kubeCommands.isFileCreated(simulator.get(0), RESULT_DIR, expectedFileName)) {
            actual = loader.run(readFileFromConsole, true);
            log.info("{expected}\n :{}", expected);
            log.info("----------------");
            log.info("{actual}\n :{}", actual);
            compare(expected, actual, false);
        } else {
            log.info("{ACTUAL FILE NOT FOUND}");
            fail();
        }
        //reader.cleanUp(reader.getFiles(Path.of(UploadType.TRANSFER.getDataDir().concat("/tmp"))), UploadType.TRANSFER.getFilePrefix());
    }

    private void compare(List<String> expected, List<String> actual, boolean ignoreVersionAndHeader) {
        if (ignoreVersionAndHeader) {
            List<String> updatedExpected =
                    IntStream.range(0, expected.size())
                            .mapToObj(i -> {
                                if (i < 2) {
                                    return actual.get(i); // keep first 2 rows unchanged
                                }
                                String[] actualParts = actual.get(i).split(",");
                                String[] expectedParts = expected.get(i).split(",");
                                expectedParts[4] = actualParts[4];
                                expectedParts[6] = actualParts[6];
                                if (actualParts.length == 10) {
                                    expectedParts[9] = actualParts[9];
                                }
                                return String.join(",", expectedParts);
                            })
                            .toList();
            assertIterableEquals(updatedExpected, actual);
        } else {
            assertIterableEquals(expected, actual);
        }
    }

}
