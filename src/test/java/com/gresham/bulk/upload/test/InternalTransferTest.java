package com.gresham.bulk.upload.test;

import com.github.javafaker.Faker;
import com.gresham.bulk.upload.BulkUploadTestProcessor;
import com.gresham.bulk.upload.UploadType;
import com.gresham.bulk.upload.entity.TransferRecord;
import com.gresham.bulk.upload.service.KubeCommands;
import com.gresham.bulk.upload.service.Loader;
import com.gresham.bulk.upload.service.QueryService;
import com.gresham.bulk.upload.service.ResourceReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Slf4j
class InternalTransferTest implements BulkUploadTestProcessor {
    @Autowired
    private KubeCommands kubeCommands;
    @Autowired
    private ResourceReader reader;
    @Autowired
    private Loader loader;
    @Autowired
    QueryService queryService;

   
    @Test
    void testInvalidAuthLinkInFileNameAndHeader() {
        String resultType = "REJECT";
        String authLink = "INVALID";
        assertFalse(authLink.isBlank(), "No customer found for this test check conditions in findCustomersOfType");
        List<String> accounts = List.of("123456789", "987654321");
        assertEquals(2, accounts.size(), "not enough accounts");
        List<String> expected = List.of(
                "Record Type,Error Text",
                "E,Row 2: Unable to retrieve Customer record for AuthLink in file name",
                "E,Row 3: Internal Transfer unable to be made due to file-level errors"
        );
        createTestFileAndCompareResult(authLink, accounts, expected, resultType);
    }

    @Test
    void testFromAccountIsBlank() {
        String resultType = "REJECT";
        String authLink = findAuthLink(queryService.findCustomersOfType("VIRTUAL_ACCOUNTS"));
        assertFalse(authLink.isBlank(), "No customer found for this test check conditions in findCustomersOfType");
        List<String> accounts = queryService.findAccountNumberWithBalanceForCustomer(authLink);
        accounts.set(0, "");
        assertEquals(2, accounts.size(), "not enough accounts");
        List<String> expected = List.of(
                "Record Type,Error Text",
                "E,All Transfer details in the file are invalid-Fatal error in transfer detail record(s)",
                "E,Row 3: missing mandatory field in column 3"
        );
        createTestFileAndCompareResult(authLink, accounts, expected, resultType);
    }

    @Test
    void testToAccountIsBlank() {
        String resultType = "REJECT";
        String authLink = findAuthLink(queryService.findCustomersOfType("VIRTUAL_ACCOUNTS"));
        logCustomerDetails(authLink);
        assertFalse(authLink.isBlank(), "No customer found for this test check conditions in findCustomersOfType");
        List<String> accounts = queryService.findAccountNumberWithBalanceForCustomer(authLink);
        assertEquals(2, accounts.size(), "NOT ENOUGH ACCOUNTS");
        accounts.set(1, "");
        List<String> expected = List.of(
                "Record Type,Error Text",
                "E,All Transfer details in the file are invalid-Fatal error in transfer detail record(s)",
                "E,Row 3: missing mandatory field in column 4"
        );
        createTestFileAndCompareResult(authLink, accounts, expected, resultType);
    }

    @Test
    void testToAccountNotFound() {
        String resultType = "RESPONSE";
        String authLink = findAuthLink(queryService.findCustomersOfType("VIRTUAL_ACCOUNTS"));
        logCustomerDetails(authLink);
        assertFalse(authLink.isBlank(), "No customer found for this test check conditions in findCustomersOfType");
        List<String> accounts = queryService.findAccountNumberWithBalanceForCustomer(authLink);
        assertEquals(2, accounts.size(), "NOT ENOUGH ACCOUNTS");
        accounts.set(1, "99999999");
        List<String> expected = List.of(
                "1,V,1.0",
                "2,H,1,0,1",
                String.format("3,T,%s,99999999,46.23,AUD,kJyVM,failed,The account to be credited from unknown", accounts.get(0))
        );
        createTestFileAndCompareResult(authLink, accounts, expected, resultType);
    }

    @Test
    void testFromAndToAccountNotFound() {
        String resultType = "RESPONSE";
        String authLink = findAuthLink(queryService.findCustomersOfType("VIRTUAL_ACCOUNTS"));
        logCustomerDetails(authLink);
        assertFalse(authLink.isBlank(), "No customer found for this test check conditions in findCustomersOfType");
        List<String> accounts = List.of("11111111", "99999999");
        assertEquals(2, accounts.size(), "NOT ENOUGH ACCOUNTS");
        List<String> expected = List.of(
                "1,V,1.0",
                "2,H,1,0,1",
                "3,T,11111111,99999999,46.23,AUD,kJyVM,validation_error,Invalid ACMC From Account Number provided,"
        );
        createTestFileAndCompareResult(authLink, accounts, expected, resultType);
    }

    @Test
    void testFromAccountNotFound() {
        String resultType = "RESPONSE";
        String authLink = findAuthLink(queryService.findCustomersOfType("VIRTUAL_ACCOUNTS"));
        logCustomerDetails(authLink);
        assertFalse(authLink.isBlank(), "No customer found for this test check conditions in findCustomersOfType");
        List<String> accounts = queryService.findAccountNumberWithBalanceForCustomer(authLink);
        assertEquals(2, accounts.size(), "NOT ENOUGH ACCOUNTS");
        accounts.set(0, "00000000");
        List<String> expected = List.of(
                "1,V,1.0",
                "2,H,1,0,1",
                String.format("3,T,00000000,%s,46.23,AUD,kJyVM,validation_error,Invalid ACMC From Account Number provided,", accounts.get(1))
        );
        createTestFileAndCompareResult(authLink, accounts, expected, resultType);
    }

    @Test
    void testValidFile() {
        String resultType = "RESPONSE";
        String authLink = findAuthLink(queryService.findCustomersOfType("VIRTUAL_ACCOUNTS"));
        logCustomerDetails(authLink);
        assertFalse(authLink.isBlank(), "No customer found for this test check conditions in findCustomersOfType");
        List<String> accounts = queryService.findAccountNumberWithBalanceForCustomer(authLink);
        assertEquals(2, accounts.size(), "NOT ENOUGH ACCOUNTS");
        List<String> expected = List.of(
                "1,V,1.0",
                "2,H,1,0,1",
                String.format("3,T,%s,%s,46.23,AUD,kJyVM,successful,,transactionId", accounts.get(0), accounts.get(1))
        );
        createTestFileAndCompareResult(authLink, accounts, expected, resultType);
    }


    @ParameterizedTest(name = "{0} test")
    @MethodSource("com.gresham.bulk.upload.dataprovider.internalTransferDataProvider#HeaderInvalidScenarios")
    void testHeaderValidations(String scenario, String resultType, String header, List<String> expected) {
        String authLink = findAuthLink(queryService.findCustomersOfType("VIRTUAL_ACCOUNTS"));
        StringBuilder fileData = new StringBuilder();
        logCustomerDetails(authLink);
        header = updateNonCustomerCodeCases(scenario, header, authLink);
        String amount = header.split(",")[4];
        fileData.append("1,V,1.0").append("\n").append(header).append("\n")
                .append(updateAmount(scenario, amount));
        assertFalse(authLink.isBlank(), "No customer found for this test check conditions in findCustomersOfType");
        createTestFileForHeaderValidationAndCompareResult(authLink, expected, fileData, resultType);
    }

   
    private String updateAmount(String scenario, String amount) {
        return scenario.equalsIgnoreCase("totalAmountNotMatching") ? "3,T,123456789,987654321,11.00,AUD,,," : String.format("3,T,123456789,987654321,%s,AUD,,,", amount);
    }


    private static String updateNonCustomerCodeCases(String testName, String header, String authLink) {
        if (!testName.equalsIgnoreCase("InvalidCustomerCode")) {
            header = String.format(header, authLink);
        }
        return header;
    }

    private static void logCustomerDetails(String authLink) {
        log.info("CUSTOMER USED FOR TEST: {}", authLink);
    }


    private String findAuthLink(List<String> customers) {
        return customers.get(ThreadLocalRandom.current().nextInt(customers.size()));
    }

    private Path createTestFile(String fileNamePrefixType, String authLink, List<String> accounts) {

        StringBuilder transferData = new StringBuilder();
        BigDecimal headerAmount = new BigDecimal(0);
        List<TransferRecord> transferRecords = processAccountsForTestFile(accounts);
        headerAmount = transferRecords.stream().map(TransferRecord::getAmount)
                .map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);

        transferData.append("1,V,1.0\n");
        transferData.append(String.format("2,H,%s,%s,%s,AUD\n", authLink, transferRecords.size(), headerAmount.toString()));

        transferRecords.forEach(t -> {
            transferData.append(t.toString());
        });
        String trasferTempDir = "src/test/resources/bulkUpload/transfer/tmp/";
        Path path = Path.of(trasferTempDir + reader.createFileName(authLink, fileNamePrefixType));
        try {
            Files.writeString(path, transferData.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return path;
    }

    private Path createTestFileHeaderCases(String fileNamePrefixType, String authLink, StringBuilder fileData) {
        String trasferTempDir = UploadType.TRANSFER.getDataDir().concat("/tmp");
        Path path = Path.of(trasferTempDir + reader.createFileName(authLink, fileNamePrefixType));
        try {
            Files.writeString(path, fileData.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return path;
    }

    private Path createTestFile(String fileNamePrefixType, String authLink, List<String> accounts, boolean isInvalidAuthLinkCase) {

        StringBuilder transferData = new StringBuilder();
        BigDecimal headerAmount = new BigDecimal(0);
        List<TransferRecord> transferRecords = processAccountsForTestFile(accounts);
        headerAmount = transferRecords.stream().map(TransferRecord::getAmount)
                .map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);

        transferData.append("1,V,1.0\n");
        if (isInvalidAuthLinkCase) {
            transferData.append(String.format("2,H,%s,%s,%s,AUD\n", "INVALID_AUTH_LINK", transferRecords.size(), headerAmount.toString()));
        } else {
            transferData.append(String.format("2,H,%s,%s,%s,AUD\n", authLink, transferRecords.size(), headerAmount.toString()));
        }
        transferRecords.forEach(t -> {
            transferData.append(t.toString());
        });
        String trasferTempDir = "src/test/resources/bulkUpload/transfer/tmp/";
        Path path = Path.of(trasferTempDir + reader.createFileName(authLink, fileNamePrefixType));
        try {
            Files.writeString(path, transferData.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return path;
    }

    private List<TransferRecord> processAccountsForTestFile(List<String> accounts) {
        List<TransferRecord> transferRecords = new LinkedList<>();
        AtomicInteger recordNumber = new AtomicInteger(3);
        BigDecimal headerAmount = new BigDecimal(0);
        accounts.stream().limit(accounts.size() / 2).forEach(
                record -> {
                    BigDecimal currentAmount = getAmount();
                    transferRecords.add(TransferRecord.builder()
                            .recordNumber(String.valueOf(recordNumber.getAndIncrement()))
                            .debitingAccountNumber(record)
                            .amount(currentAmount.toString())
                            .currency("AUD")
                            .customerTransactionReference(getRefText(5, false))
                            .payerReference(getRefText(5, false))
                            .payeeReference(getRefText(5, false))
                            .build());
                }

        );
        AtomicInteger transferRecordsSize = new AtomicInteger(transferRecords.size());
        transferRecords.forEach(transferRecord -> {
            transferRecord.setCreditingAccountNumber(accounts.get(transferRecordsSize.getAndIncrement()));
        });

        return transferRecords;
    }

    void createTestFileAndCompareResult(String authLink, List<String> accounts, List<String> expected, String resultType) {
        assertFalse(authLink.isBlank(), "No customer found for this test check conditions in findCustomersOfType");
        assertTrue(accounts.size() == 2, "not enough accounts");
        List<String> actual;
        Path testFile = createTestFile(UploadType.TRANSFER.getFilePrefix(), authLink, accounts);
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
            compare(expected, actual, true);
        } else {
            log.info("{ACTUAL FILE NOT FOUND}");
            fail();
        }
        //reader.cleanUp(reader.getFiles(Path.of(UploadType.TRANSFER.getDataDir().concat("/tmp"))), UploadType.TRANSFER.getFilePrefix());
    }

    void createTestFileForHeaderValidationAndCompareResult(String authLink, List<String> expected, StringBuilder fileData, String resultType) {
        assertFalse(authLink.isBlank(), "No customer found for this test check conditions in findCustomersOfType");
        List<String> actual;
        Path testFile = createTestFileHeaderCases(UploadType.TRANSFER.getFilePrefix(), authLink, fileData);
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


    void createTestFileAndCompareResult(String authLink, List<String> accounts, List<String> expected, String
            resultType, boolean isInvalidAuthLinkCase) {
        try {
            assertFalse(authLink.isBlank(), "No customer found for this test check conditions in findCustomersOfType");
            assertTrue(accounts.size() == 2, "not enough accounts");
            List<String> actual;
            Path testFile = createTestFile(UploadType.TRANSFER.getFilePrefix(), authLink, accounts, isInvalidAuthLinkCase);
            postFileAndCompareResult(testFile, expected, resultType);
        } catch (Exception e) {
            log.error("Error in createTestFileAndCompareResult", e);
            fail();
        } finally {
            reader.cleanUp(reader.getFiles(Path.of(UploadType.TRANSFER.getDataDir().concat("/tmp"))), UploadType.TRANSFER.getFilePrefix());
        }
    }

    private void postFileAndCompareResult(Path testFile, List<String> expected, String resultType) {
        List<String> actual;
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
    }

    public Path createTestFile(Path scenarioFile, String fileNamePrefixType) {
        String authLink;
        String fileFullPath = scenarioFile.getFileName().toString();
        if (fileFullPath.endsWith("data.csv")) {
            try (java.util.stream.Stream<String> lines = Files.lines(scenarioFile)) {
                String authLinkLine = lines.skip(1).findFirst().orElse("");
                authLink = Arrays.asList(authLinkLine.split(",")).get(2);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            authLink = fileFullPath.substring(0, fileFullPath.lastIndexOf("."));
        }
        Path target = Path.of(scenarioFile.getParent().toString().concat("/" + reader.createFileName(authLink, fileNamePrefixType)));
        try {
            Files.copy(scenarioFile, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return target;
    }

    public Path createTestFile(Path scenarioFile, String fileNamePrefixType, String customer) {
        String authLink = "";
        String authLinkLine;
        Path target;
        String fileFullPath = scenarioFile.getFileName().toString();
        if (fileFullPath.endsWith("data.csv")) {
            try (java.util.stream.Stream<String> lines = Files.lines(scenarioFile)) {
                authLinkLine = lines.skip(1).findFirst().orElse("");
                if (customer != null && !customer.isEmpty()) {
                    authLinkLine = customer;
                    List<String> data = Files.readAllLines(scenarioFile);
                    List<String> updated = new LinkedList<>();
                    data.forEach(line -> {
                        if (line.startsWith("2,H,")) {
                            line = line.replaceAll("(^2,H,)[^,]*,", "$1" + customer + ",");
                        }
                        updated.add(line);
                    });
                    target = Path.of(scenarioFile.getParent().toString().concat("/" + reader.createFileName(authLinkLine, fileNamePrefixType)));
                    Files.write(target, updated);
                    return target;
                } else {
                    authLink = Arrays.asList(authLinkLine.split(",")).get(2);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            authLink = fileFullPath.substring(0, fileFullPath.lastIndexOf("."));
        }
        target = Path.of(scenarioFile.getParent().toString().concat("/" + reader.createFileName(authLink, fileNamePrefixType)));
        try {
            Files.copy(scenarioFile, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return target;
    }

    public BigDecimal getAmount() {
        return BigDecimal
                .valueOf(ThreadLocalRandom.current().nextDouble(10, 50))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public String getRefText(int length, boolean includeNumber) {
        String characters = Faker.instance().lorem().characters(length, true, includeNumber);
        return length == 0 ? "" : characters;
    }
}
