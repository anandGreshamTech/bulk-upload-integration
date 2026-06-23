package com.gresham.bulk.upload.test;

import com.github.javafaker.Faker;
import com.gresham.bulk.upload.BulkUploadTestProcessor;
import com.gresham.bulk.upload.UploadType;
import com.gresham.bulk.upload.compare.DataCompareService;
import com.gresham.bulk.upload.dto.ProductDetail;
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
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("tui")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Slf4j
class InternalTransfer implements BulkUploadTestProcessor {
    @Autowired
    private KubeCommands kubeCommands;
    @Autowired
    private ResourceReader reader;
    @Autowired
    private Loader loader;
    @Autowired
    QueryService queryService;


    @ParameterizedTest(name = "{0} test")
    @MethodSource("com.gresham.bulk.upload.dataprovider.InternalTransferDataProvider#data")
    void transferValidations(String scenario, String resultType, boolean ignoreHeader, Map<Integer, List<Integer>> columnsToIgnoreByRow, List<String> data, List<String> expected) {
        var authLink = findAuthLink(queryService.findCustomersOfType("VIRTUAL_ACCOUNTS"));
        authLink = getAuthLinkForNegativeCases(scenario, authLink);
        var fileData = new StringBuilder();
        logCustomerDetails(authLink);
        var header = updateNonCustomerCodeCases(scenario, data.get(1), authLink);
        List<String> accountNumberWithBalance = queryService.findAccountNumberWithBalance(authLink, 100, 2);
        List<String> transferRecordList= data.subList(2, data.size());
        var transferRecord = updateAccounts(transferRecordList,accountNumberWithBalance);
        transferRecord = updateReference(transferRecord);
        fileData.append(data.get(0)).append("\n").append(header).append("\n");
        transferRecord.forEach(record -> fileData.append(record).append("\n"));
        assertFalse(authLink.isBlank(), "No customer found for this test check conditions in findCustomersOfType");
        authLink=updateAuthLinkBasedOnScenario(scenario, authLink);
        createTestFileForHeaderValidationAndCompareResult(authLink, expected, fileData, resultType,ignoreHeader, columnsToIgnoreByRow);
    }
    
    private String updateAuthLinkBasedOnScenario(String scenario, String authLink) {
       if(scenario.equalsIgnoreCase("fileNameAuthLinkIsNotValid")) {
           return "invalidAuthLink";
       }else if(scenario.equalsIgnoreCase("fileNameAuthLinkIsDifferentThanDataInFile")) {
           return queryService.findCustomersNotIn(authLink);
       }
        return authLink;
    }

    private List<String> updateAccounts(List<String> transferRecords,List<String> accounts){
        if(accounts.size()<2){
            fail("No accounts found");
        }
        List<String> result = new ArrayList<>();
        transferRecords.forEach(record -> {
            var updatedRecord = record.replace("${from}", accounts.get(0)).replace("${to}", accounts.get(1));
            result.add(updatedRecord);
        });
        return result;
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

    private String updateNonCustomerCodeCases(String testName, String header, String authLink) {
        if (!testName.equalsIgnoreCase("InvalidCustomerCode")) {
            header = header.replace("${authlink}", authLink);
        }
        return header;
    }

      
    private List<String> updateReference(List<String> transferRecords) {
        Faker faker = new Faker();
        List<String> result = new ArrayList<>();
            transferRecords.forEach(record -> {
                var updatedRecord = record.replace("${custRef}", faker.number().digits(6)).replace("${payerRef}", faker.number().digits(6)).replace("${payeeRef}", faker.number().digits(9));
            result.add(updatedRecord);
            });
       
        return result;
    }
    private String updateAccountName(String accountOpenRecord) {
        Faker faker = new Faker();
        var fullName = faker.name().fullName();
        return accountOpenRecord.replace("${accountName}", fullName);
    }

    private void logCustomerDetails(String authLink) {
        log.info("CUSTOMER USED FOR TEST: {}", authLink);
    }

    private String findAuthLink(List<String> customers) {
        return customers.get(ThreadLocalRandom.current().nextInt(customers.size()));
    }


    private Path createTestFileHeaderCases(String fileNamePrefixType, String authLink, StringBuilder fileData) {
        String tempDir = UploadType.TRANSFER.getDataDir().concat("/tmp/");
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


