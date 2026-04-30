package com.gresham.bulk.upload.test;

import com.github.javafaker.Faker;
import com.gresham.bulk.upload.BulkUploadTestProcessor;
import com.gresham.bulk.upload.UploadType;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Slf4j
class VamOpenAccountV2Test implements BulkUploadTestProcessor {
    @Autowired
    private KubeCommands kubeCommands;
    @Autowired
    private ResourceReader reader;
    @Autowired
    private Loader loader;
    @Autowired
    QueryService queryService;


    @ParameterizedTest(name = "{0} test")
    @MethodSource("com.gresham.bulk.upload.dataprovider.OpenAccountDataProvider#accountOpenRecordValidations")
    void testHeaderValidations(String scenario, String resultType, List<String> data, List<String> expected) {
        var authLink = findAuthLink(queryService.findCustomersOfType("VIRTUAL_ACCOUNTS"));
        authLink = getAuthLinkForNegativeCases(scenario, authLink);
        var fileData = new StringBuilder();
        logCustomerDetails(authLink);
        var header = updateNonCustomerCodeCases(scenario, data.get(1), authLink);
        var product = queryService.findProductForCustomer(authLink);
        var accountOpenRecord = updateProductDetailsInAccountRecord(data.get(2), product);
        accountOpenRecord = updateReference(accountOpenRecord);
        fileData.append(data.get(0)).append("\n").append(header).append("\n").append(accountOpenRecord);
        assertFalse(authLink.isBlank(), "No customer found for this test check conditions in findCustomersOfType");
        createTestFileForHeaderValidationAndCompareResult(authLink, expected, fileData, resultType);
    }

    private String getAuthLinkForNegativeCases(String scenario, String authLink) {
        if (scenario.equalsIgnoreCase("vamAccountOpenRequestWithClientMoneyCustomer")) {
            authLink = getClientMoniesCustomer();
        }else if (scenario.equalsIgnoreCase("fileNameAuthLinkNotPresent&NotMatchingWithHeader")) {
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

    private String updateProductDetailsInAccountRecord(String accountOpenRecord, List<ProductDetail> productDetail) {
        var random = ThreadLocalRandom.current().nextInt(0, productDetail.size());
        ProductDetail currentPrd = productDetail.get(random);
        return accountOpenRecord.replace("${productId}", currentPrd.id()).replace("${productCode}", currentPrd.code());
    }

    private String updateReference(String accountOpenRecord) {
        Faker faker = new Faker();
        var ref = faker.number().digits(6);
        var secRef = faker.number().digits(9);

        return accountOpenRecord.replace("${reference}", ref).replace("${secRed}", secRef);
    }

    private void logCustomerDetails(String authLink) {
        log.info("CUSTOMER USED FOR TEST: {}", authLink);
    }

    private String findAuthLink(List<String> customers) {
        return customers.get(ThreadLocalRandom.current().nextInt(customers.size()));
    }


    private Path createTestFileHeaderCases(String fileNamePrefixType, String authLink, StringBuilder fileData) {
        String tempDir = UploadType.VAM_OPEN_ACCOUNT.getDataDir().concat("/tmp/");
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
        Path testFile = createTestFileHeaderCases(UploadType.VAM_OPEN_ACCOUNT.getFilePrefix(), authLink, fileData);
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
            compare(expected, actual);
        } else {
            log.info("{ACTUAL FILE NOT FOUND}");
            fail();
        }
//        reader.cleanUp(reader.getFiles(Path.of(UploadType.VAM_OPEN_ACCOUNT.getDataDir().concat("/tmp"))), UploadType.VAM_OPEN_ACCOUNT.getFilePrefix());
    }

    private void compare(List<String> expected, List<String> actual) {

        List<String> updatedExpected =
                IntStream.range(0, expected.size())
                        .mapToObj(i -> {
                            List<String> actualParts;
                            List<String> expectedParts = new ArrayList<>();
                            if (i == 1) {
                                actualParts = Arrays.asList(actual.get(i).split(","));
                                expectedParts = Arrays.asList(expected.get(i).split(","));
                                if (expectedParts.size() > 2) {
                                    expectedParts.set(2, actualParts.get(2));
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


