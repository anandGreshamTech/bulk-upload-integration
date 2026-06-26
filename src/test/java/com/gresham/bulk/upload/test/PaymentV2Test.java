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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("tui")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Slf4j
class PaymentV2Test implements BulkUploadTestProcessor {
    @Autowired
    private KubeCommands kubeCommands;
    @Autowired
    private ResourceReader reader;
    @Autowired
    private Loader loader;
    @Autowired
    QueryService queryService;

    @ParameterizedTest(name = "{0}Test")
    @MethodSource("com.gresham.bulk.upload.dataprovider.PaymentDataProvider#data")
    void paymentTest(String scenario, String resultType, boolean ignoreHeader, Map<Integer, List<Integer>> columnsToIgnoreByRow, List<String> data, List<String> expected) {
//        var authLink = findAuthLink(queryService.findCustomersOfType("CLIENT_MONIES"));
        var authLink = "BULK2";
        var fileData = new StringBuilder();
        logCustomerDetails(authLink);
        List<String> accounts = queryService.findAccountNumberWithBalance(authLink,100,1);
        var header = updateNonCustomerCodeCases(scenario, data.get(1), authLink);
        List<String> instructions = data.subList(2, data.size());
        var paymentRecord = updateAccountNumberAndReferenceInPaymentRecord(instructions, accounts);
        //paymentRecord = UpdateRecipientDetailsInCloseAccountRecord(paymentRecord, true, true, true);
        fileData.append(data.get(0)).append("\n").append(header).append("\n");
        paymentRecord.forEach(record -> fileData.append(record).append("\n"));
        assertFalse(authLink.isBlank(), "No customer found for this test check conditions in findCustomersOfType");
        authLink = getAuthLinkForNegativeCase(scenario, authLink);
        createTestFileForHeaderValidationAndCompareResult(authLink, expected, fileData, resultType, ignoreHeader, columnsToIgnoreByRow);
    }

    String getAuthLinkForNegativeCase(String scenario, String authLink) {
        return "incorrectAuthLinkInFileName".equalsIgnoreCase(scenario) ? "invalidAuthLink" : authLink;
    }

    private String updateNonCustomerCodeCases(String testName, String header, String authLink) {
        if (!testName.equalsIgnoreCase("InvalidCustomerCode")) {
            header = header.replace("${authlink}", authLink);
        }
        return header;
    }

    private List<String> updateAccountNumberAndReferenceInPaymentRecord(List<String> paymentRecord, List<String> accounts) {
        return paymentRecord.stream().map(record -> record.replace("${account}", accounts.get(0)))
                .map(record -> record.replace("${reference}", Faker.instance().bothify("??##??##")))
                .map(record ->record.replace("${valueDate}",LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE))).toList();
                
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
        String tempDir = UploadType.PAYMENT.getDataDir().concat("/tmp/");
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
        Path testFile = createTestFileHeaderCases(UploadType.PAYMENT.getFilePrefix(), authLink, fileData);
        System.out.println("kubectl cp -n coil ".concat(testFile.toAbsolutePath().toString()));
        System.out.println(String.format("comms-in-0:data/fileactive/bulkupload/waiting/%s -c comm-anz-in\n",testFile.getFileName()));
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

}
    


