package com.gresham.bulk.upload.test;

import com.github.javafaker.Faker;
import com.gresham.bulk.upload.BulkUploadTestProcessor;
import com.gresham.bulk.upload.UploadType;
import com.gresham.bulk.upload.compare.DataCompareService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static com.gresham.bulk.upload.UploadType.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("tui")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Slf4j
class CloseAccount implements BulkUploadTestProcessor {
    @Autowired
    private KubeCommands kubeCommands;
    @Autowired
    private ResourceReader reader;
    @Autowired
    private Loader loader;
    @Autowired
    QueryService queryService;


    @ParameterizedTest(name = "{0} test")
    @MethodSource("com.gresham.bulk.upload.dataprovider.CloseAccountDataProviderV2#data")
    void transferValidations(String scenario, String resultType, boolean ignoreHeader, Map<Integer, List<Integer>> columnsToIgnoreByRow, List<String> data, List<String> expected) {
        var authLink = findAuthLink(queryService.findCustomersOfType("CLIENT_MONIES"));
        authLink = getAuthLinkForNegativeCases(scenario, authLink);
        var fileData = new StringBuilder();
        logCustomerDetails(authLink);
        var header = updateAuthLink(data.get(1), authLink);
        List<String> accountNumberWithBalance = queryService.findAccountNumberWithBalance(authLink, 0, 1);
        List<String> closureRecordList = data.subList(2, data.size());
        var updatedClosureRecord = updateAccounts(closureRecordList, accountNumberWithBalance);
        updatedClosureRecord=updateRecipient(updatedClosureRecord);
        fileData.append(data.get(0)).append("\n").append(header).append("\n");
        updatedClosureRecord.forEach(record -> fileData.append(record).append("\n"));
        assertFalse(authLink.isBlank(), "No customer found for this test check conditions in findCustomersOfType");
        authLink = updateAuthLinkBasedOnScenario(scenario, authLink);
        createTestFileForHeaderValidationAndCompareResult(authLink, expected, fileData, resultType, ignoreHeader, columnsToIgnoreByRow);
    }


    private String updateAuthLinkBasedOnScenario(String scenario, String authLink) {
        if (scenario.equalsIgnoreCase("fileNameAuthLinkIsNotValid")) {
            return "invalidAuthLink";
        } else if (scenario.equalsIgnoreCase("fileNameAuthLinkIsDifferentThanDataInFile")) {
            return queryService.findCustomersNotIn(authLink);
        }
        return authLink;
    }

    private List<String> updateAccounts(List<String> records, List<String> accounts) {
        if (accounts.size() < records.size()) {
            fail("Dont Have required number of accounts to run this test, check the conditions in data provider ");
        }
        return IntStream.range(0, records.size())
                .mapToObj(i -> records.get(i).replace("${account}", accounts.get(i)))
                .toList();
    }

    private String getAuthLinkForNegativeCases(String scenario, String authLink) {
        if (scenario.equalsIgnoreCase("vamAccountOpenRequestWithClientMoneyCustomer")) {
            authLink = getClientMoniesCustomer();
        } else if (scenario.equalsIgnoreCase("fileNameAuthLinkNotPresent&NotMatchingWithHeader")) {
            authLink = "invalidAuthLink";
        }
        return authLink;
    }

    private String getClientMoniesCustomer() {
        return findAuthLink(queryService.findCustomersOfType("CLIENT_MONIES"));
    }

    private String updateAuthLink(String header, String authLink) {
        return header.replace("${authlink}", authLink);
    }


    private List<String> updateRecipient(List<String> transferRecords) {
        Faker faker = new Faker();
        List<String> result = new ArrayList<>();
        return IntStream.range(0, transferRecords.size())
                .mapToObj(i -> 
                     transferRecords.get(i).replace("${recipientName}", faker.name().fullName())
                             .replace("${recipientAccount}", faker.number().digits(9))
                             .replace("${bsb}", faker.number().digits(6))).toList();
        
    }

    private void logCustomerDetails(String authLink) {
        log.info("CUSTOMER USED FOR TEST: {}", authLink);
    }

    private String findAuthLink(List<String> customers) {
        return customers.get(ThreadLocalRandom.current().nextInt(customers.size()));
    }


    private Path createTestFileHeaderCases(String fileNamePrefixType, String authLink, StringBuilder fileData) {
        String tempDir = CLOSE_ACCOUNT.getDataDir().concat("/tmp/");
        Path path = Path.of(tempDir + reader.createFileName(authLink, fileNamePrefixType));
        try {
            Files.writeString(path, fileData.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return path;
    }

    void createTestFileForHeaderValidationAndCompareResult(String authLink, List<String> expected, StringBuilder fileData, String resultType, boolean ignoreHeader, Map<Integer, List<Integer>> columnsToIgnoreByRow) {
        DataCompareService dataCompare = new DataCompareService();
        assertFalse(authLink.isBlank(), "No customer found for this test check conditions in findCustomersOfType");
        List<String> actual;
        Path testFile = createTestFileHeaderCases(CLOSE_ACCOUNT.getFilePrefix(), authLink, fileData);
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
            dataCompare.assertFileData(
                    expected.stream().map(line -> List.of(line.split(","))).toList(),
                    actual.stream().map(line -> List.of(line.split(","))).toList(),
                    ignoreHeader,
                    columnsToIgnoreByRow
            );
        } else {
            log.info("{ACTUAL FILE NOT FOUND}");
            fail();
        }
//        reader.cleanUp(reader.getFiles(Path.of(UploadType.TRANSFER.getDataDir().concat("/tmp"))), UploadType.TRANSFER.getFilePrefix());
    }

    private void compare(List<String> expected, List<String> actual) {

        List<String> updatedExpected =
                IntStream.range(0, expected.size())
                        .mapToObj(i -> {
                            List<String> actualParts;
                            List<String> expectedParts = new ArrayList<>();
                            actualParts = Arrays.asList(actual.get(i).split(","));
                            expectedParts = Arrays.asList(expected.get(i).split(","));
                            if (i == 1) { // this is header row where we need to update authlink
                                if (expectedParts.size() > 2) {
                                    expectedParts.set(2, actualParts.get(2));
                                }
                            } else if (i == 2) {// this is for account record account id and number handling
                                if (expectedParts.size() > 2 && expectedParts.get(2).equalsIgnoreCase("success")) {
                                    expectedParts.set(3, actualParts.get(3));
                                    expectedParts.set(4, actualParts.get(4));
                                }

                            } else {
                                expectedParts = Arrays.asList(expected.get(i).split(","));
                            }
                            return String.join(",", expectedParts);
                        })
                        .toList();

        assertIterableEquals(updatedExpected, actual);
    }

}


