package com.gresham.bulk.upload.test;

import com.github.javafaker.Faker;
import com.gresham.bulk.upload.BulkUploadTestProcessor;
import com.gresham.bulk.upload.UploadType;
import com.gresham.bulk.upload.dto.ClaDetail;
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
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static com.gresham.bulk.upload.UploadType.*;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("tui")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Slf4j
class AggregateReceiptV2Test implements BulkUploadTestProcessor {
    @Autowired
    private KubeCommands kubeCommands;
    @Autowired
    private ResourceReader reader;
    @Autowired
    private Loader loader;
    @Autowired
    QueryService queryService;


    @ParameterizedTest(name = "{0}Test")
    @MethodSource("com.gresham.bulk.upload.dataprovider.AggregateReceiptDataProvider#data")
    void aggregateReceipt(String scenario, String resultType, String claUpdateType, boolean ignoreHeader, Map<Integer, List<Integer>> columnsToIgnoreByRow, List<String> data, List<String> expected) {
//        var authLink = findAuthLink(queryService.findCustomersOfType("CLIENT_MONIES"));
        var authLink = "BULK2";
        var fileData = new StringBuilder();
        logCustomerDetails(authLink);
        List<String> accounts = getAccountNumber(scenario, authLink);
        List<ClaDetail> cla = queryService.findClaForCustomer(authLink);
        var header = updateCustomerCodeCases(scenario, data.get(1), authLink);
        header = updateCla(scenario, header, cla.get(0), claUpdateType);
        header = updateReference(scenario, header);
        List<String> instructions = data.subList(2, data.size());
        var aggregateInstructions = updateAccountNumberInPaymentRecord(instructions, accounts);
        fileData.append(data.get(0)).append("\n").append(header).append("\n");
        aggregateInstructions.forEach(line -> fileData.append(line).append("\n"));
        assertFalse(authLink.isBlank(), "No customer found for this test check conditions in findCustomersOfType");
        authLink = getAuthLinkForNegativeCase(scenario, authLink);
        createTestFileForHeaderValidationAndCompareResult(authLink, expected, fileData, resultType, ignoreHeader, columnsToIgnoreByRow);
    }

    private List<String> getAccountNumber(String scenario, String authLink) {
        List<String> accounts = Collections.emptyList();
        if (scenario.toUpperCase().contains("INSUFFICIENTFUNDS")) {
            accounts = queryService.findAccountNumberWithBalance(authLink, 0,1);
        } else {
            accounts = queryService.findAccountNumberWithBalance(authLink, 100,1);
        }
        return accounts;
    }

    String getAuthLinkForNegativeCase(String scenario, String authLink) {
        return "incorrectAuthLinkInFileName".equalsIgnoreCase(scenario) ? "invalidAuthLink" : authLink;
    }

    private String updateCustomerCodeCases(String testName, String header, String authLink) {
        if (!testName.equalsIgnoreCase("InvalidCustomerCode")) {
            header = header.replace("${authlink}", authLink);
        }
        return header;
    }

    private String updateCla(String scenario, String header, ClaDetail cla, String claUpdateType) {
        if (claUpdateType.equalsIgnoreCase("claName")) {
            header = header.replace("${claName}", cla.name());
        } else if (claUpdateType.equalsIgnoreCase("claId")) {
            header = header.replace("${claId}", cla.id());
        } else {
            header = header.replace("${claName}", cla.name()).replace("${claId}", cla.id());

        }
        return header;
    }

    private String updateReference(String scenario, String header) {
        Faker faker = new Faker();
        var reference = faker.bothify("???-##-????");

        return header.replace("${reference}", reference);
    }

    private List<String> updateAccountNumberInPaymentRecord(List<String> instruction, List<String> accounts) {
        return instruction.stream()
                .map(line -> line.replace("${account}", accounts.get(0)))
                .toList();
    }

    private String UpdateRecipientDetailsInCloseAccountRecord(String closeAccountRecord, boolean updateBsb,
                                                              boolean updateRecipientAccount, boolean updateRecipientName) {

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
        String tempDir = AGG_RECEIPT.getDataDir().concat("/tmp/");
        Path path = Path.of(tempDir + reader.createFileName(authLink, fileNamePrefixType));
        try {
            Files.writeString(path, fileData.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return path;
    }

    void createTestFileForHeaderValidationAndCompareResult(String authLink, List<String> expected, StringBuilder
            fileData, String resultType, boolean ignoreHeader, Map<Integer, List<Integer>> columnsToIgnoreByRow) {
        assertFalse(authLink.isBlank(), "No customer found for this test check conditions in findCustomersOfType");
        List<String> actual;
        Path testFile = createTestFileHeaderCases(AGG_RECEIPT.getFilePrefix(), authLink, fileData);
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
//            compare(expected, actual);
            assertFileData(
                    expected.stream().map(line -> List.of(line.split(","))).toList(),
                    actual.stream().map(line -> List.of(line.split(","))).toList(),
                    ignoreHeader,
                    columnsToIgnoreByRow
            );
        } else {
            log.info("{ACTUAL FILE NOT FOUND}");
            fail();
        }
        //reader.cleanUp(reader.getFiles(Path.of(UploadType.TRANSFER.getDataDir().concat("/tmp"))), UploadType.TRANSFER.getFilePrefix());
    }

    private void compare(List<String> expected, List<String> actual) {

        List<String> updatedExpected =
                IntStream.range(0, expected.size())
                        .mapToObj(i -> {
                            String[] actualParts = actual.get(i).split(",");
                            String[] expectedParts = expected.get(i).split(",");
                            if (actualParts.length > 2) {
                                expectedParts[2] = actualParts[2];
                                int index = 3;
                                if (index < actualParts.length && index < expectedParts.length) {
                                    expectedParts[index] = actualParts[index];
                                }
                            }
                            return String.join(",", expectedParts);

                        })
                        .toList();
        assertIterableEquals(updatedExpected, actual);

    }


    private void assertFileData(
            List<List<String>> expected,
            List<List<String>> actual,
            boolean ignoreHeader,
            Map<Integer, List<Integer>> columnsToIgnoreByRow
    ) {
        assertEquals(expected.size(), actual.size(), "Row count mismatch");

        int startRow = ignoreHeader ? 1 : 0;

        for (int row = startRow; row < expected.size(); row++) {

            List<String> expectedRow = expected.get(row);
            List<String> actualRow = actual.get(row);

            assertEquals(
                    expectedRow.size(),
                    actualRow.size(),
                    "Column count mismatch at row " + (row + 1)
            );

            List<Integer> columnsToIgnore =
                    columnsToIgnoreByRow.getOrDefault(row + 1, Collections.emptyList());

            for (int col = 0; col < expectedRow.size(); col++) {

                int columnNumber = col + 1;

                if (columnsToIgnore.contains(columnNumber)) {
                    continue;
                }

                assertEquals(
                        expectedRow.get(col),
                        actualRow.get(col),
                        "Mismatch at row " + (row + 1)
                        + ", column " + columnNumber
                );
            }
        }
    }
}
    


