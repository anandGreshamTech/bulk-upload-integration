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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("tui")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Slf4j
class OpenAccountCmTest implements BulkUploadTestProcessor {
    @Autowired
    private KubeCommands kubeCommands;
    @Autowired
    private ResourceReader reader;
    @Autowired
    private Loader loader;
    @Autowired
    QueryService queryService;


    @ParameterizedTest(name = "{0} test")
    @MethodSource("com.gresham.bulk.upload.dataprovider.OpenAccountClientMoniesDataProvider#data")
    void testClientMoniesAccountOpenValidations(String scenario, String resultType, boolean ignoreHeader, Map<Integer, List<Integer>> columnsToIgnoreByRow, List<String> data, List<String> expected) {
//        var authLink = findAuthLink(queryService.findCustomersOfType("CLIENT_MONIES"));
        var authLink = "BULK2";
        authLink = getAuthLinkForNegativeCases(scenario, authLink);
        var fileData = new StringBuilder();
        logCustomerDetails(authLink);
//        var product = queryService.findProductForCustomer(authLink);
        var product = List.of(new ProductDetail("BULK2","UHJvZHVjdDo5YWI5Yzc5YTE4YzZjMzdkZThiNTY1YTM3MWE4NTNiNzQ3OTlkMjc1NGU4Y2ExNTkxZjIwYmQ3MzUwNzYwODU2"));
        updateRecords(data, product, authLink).forEach(record -> fileData.append(record).append("\n"));
        assertFalse(authLink.isBlank(), "No customer found for this test check conditions in findCustomersOfType");
        createTestFileForHeaderValidationAndCompareResult(authLink, expected, fileData, resultType, ignoreHeader, columnsToIgnoreByRow);
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

/*
    private List<String> updateRecords(
            List<String> testData,
            List<ProductDetail> productDetail,String authLink
            
    ) {
        List<String> beneficiaryRecordTypes = List.of("I", "C", "T","S","P","O","G");
        if (productDetail == null || productDetail.isEmpty()) {
            throw new IllegalArgumentException("productDetail must not be null or empty");
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(productDetail.size());
        ProductDetail currentPrd = productDetail.get(randomIndex);

        List<String> result = new ArrayList<>();

        testData.stream()
                .map(record -> Arrays.asList(record.split(",", -1)))
                .forEach(row -> {
                    if (row.get(1).equalsIgnoreCase("A")) {

                        if (row.get(2).equalsIgnoreCase("${accountName}")) {
                            row.set(2, Faker.instance().name().fullName());
                        }
                        if (row.get(3).equalsIgnoreCase("${productId}")) {
                            row.set(3, currentPrd.id());
                        }
                        if (row.get(4).equalsIgnoreCase("${productCode}")) {
                            row.set(4, currentPrd.code());
                        }
                        if (row.get(5).equalsIgnoreCase("${reference1}")) {
                            row.set(5, Faker.instance().bothify("???-##-????"));
                        }                        
                        if (row.get(6).equalsIgnoreCase("${reference2}")) {
                            row.set(6, Faker.instance().bothify("???-##-????"));
                        }
                        result.add(String.join(",", row));
                    }
                    else if (row.get(1).equalsIgnoreCase("H")) {
                        if (row.get(2).equalsIgnoreCase("${authlink}")) {
                            row.set(2, authLink);
                        }
                        result.add(String.join(",", row));
                    }else if (beneficiaryRecordTypes.contains(row.get(1))) {

                        if (row.get(4).equalsIgnoreCase("${fullName}")) {
                            row.set(4, Faker.instance().company().name());
                        }
                        if (row.get(8).equalsIgnoreCase("${beneficiaryRef}")) {
                            row.set(8, Faker.instance().bothify("???-##-????"));
                        }
                        result.add(String.join(",", row));
                    }else{
                        result.add(String.join(",", row));
                    }

                    
                });

        return result;
    }
*/


    private List<String> updateRecords(
            List<String> testData,
            List<ProductDetail> productDetail,
            String authLink
    ) {
        List<String> beneficiaryRecordTypes = List.of("I", "C", "T", "S", "P", "O", "G");
        if (productDetail == null || productDetail.isEmpty()) {
            throw new IllegalArgumentException("productDetail must not be null or empty");
        }

        ProductDetail currentPrd = productDetail.get(
                ThreadLocalRandom.current().nextInt(productDetail.size())
        );

        return testData.stream()
                .map(record -> {
                    List<String> row = new ArrayList<>(Arrays.asList(record.split(",", -1)));
                    String recordType = row.get(1).toUpperCase();

                    switch (recordType) {
                        case "A" -> updateAccountRecord(row, currentPrd);
                        case "H" -> updateHeaderRecord(row, authLink);
                        default -> {
                            if (beneficiaryRecordTypes.contains(recordType)) {
                                updateBeneficiaryRecord(row);
                            }
                        }
                    }
                    return String.join(",", row);
                })
                .toList();
    }

    private void updateAccountRecord(List<String> row, ProductDetail product) {
        replacePlaceholder(row, 2, "${accountName}", () -> Faker.instance().company().bs());
        replacePlaceholder(row, 3, "${productId}", product::id);
        replacePlaceholder(row, 4, "${productCode}", product::code);
        replacePlaceholder(row, 5, "${reference1}", () -> Faker.instance().bothify("???-##-????"));
        replacePlaceholder(row, 6, "${reference2}", () -> Faker.instance().bothify("???-##-????"));
    }

    private void updateHeaderRecord(List<String> row, String authLink) {
        replacePlaceholder(row, 2, "${authlink}", () -> authLink);
    }

    private void updateBeneficiaryRecord(List<String> row) {
        replacePlaceholder(row, 4, "${fullName}", () -> Faker.instance().company().bs());
        replacePlaceholder(row, 7, "${beneficiaryRef}", () -> Faker.instance().bothify("???-##-????"));
    }

    private void replacePlaceholder(List<String> row, int index, String placeholder, Supplier<String> valueSupplier) {
        if (index < row.size() && row.get(index).equalsIgnoreCase(placeholder)) {
            row.set(index, valueSupplier.get());
        }
    }
    
    
    
    
    private String updateReference(String accountOpenRecord) {
        Faker faker = new Faker();
        var ref = faker.number().digits(6);
        var secRef = faker.number().digits(9);

        return accountOpenRecord.replace("${reference}", ref).replace("${secRed}", secRef);
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
        String tempDir = UploadType.CM_OPEN_ACCOUNT.getDataDir().concat("/tmp/");
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
        Path testFile = createTestFileHeaderCases(UploadType.CM_OPEN_ACCOUNT.getFilePrefix(), authLink, fileData);
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
//        reader.cleanUp(reader.getFiles(Path.of(UploadType.VAM_OPEN_ACCOUNT.getDataDir().concat("/tmp"))), UploadType.VAM_OPEN_ACCOUNT.getFilePrefix());
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


