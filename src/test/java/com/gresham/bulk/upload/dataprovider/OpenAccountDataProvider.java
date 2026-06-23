package com.gresham.bulk.upload.dataprovider;


import org.junit.jupiter.params.provider.Arguments;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class OpenAccountDataProvider {
    static Stream<Arguments> fullDataSet() {
        return Stream.of(
                Arguments.of("vamAccountOpenPositiveFlow", "RESPONSE", false, Map.of(3, List.of(4, 5)),
                        List.of(
                                "1,V,VAM_1.0",
                                "2,H,${authlink},1",
                                "3,A,${accountName},${productId},${productCode},${reference},${secRed}"


                        ), List.of(
                                "1,V,VAM_1.0",
                                "2,H,vamcust1,1,0",
                                "3,A,SUCCESS,QWNjb3VudDo3NzIzM2Y3YTg3NTU0YjZmM2U1MjJjOTc1Y2ZjYTRjOTJkODdjNzBjYTZmZWYyMTEzZWQxNjk4YTI5MWUwMmMx,700000001,0"
                        )
                ),
                Arguments.of("invalidOpenAccountRecordType", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,VAM_1.0",
                                "2,H,${authlink},1",
                                "3,B,TestUser,${productId},${productCode},${reference},${secRed}"


                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 3: Unknown record type"
                        )), 
                Arguments.of("recordTypeIsBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,VAM_1.0",
                                "2,H,${authlink},1",
                                "3,,TestUser,${productId},${productCode},${reference},${secRed}"


                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 3: Unknown record type"
                        )
                ), Arguments.of("recordNumberIsOutOfSequence", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,VAM_1.0",
                                "2,H,${authlink},1",
                                "4,A,TestUser,${productId},${productCode},${reference},${secRed}"


                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 3: Invalid record number 4"
                        )
                ), Arguments.of("accountNameIsBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,VAM_1.0",
                                "2,H,${authlink},1",
                                "3,A,,${productId},${productCode},${reference},${secRed}"


                        ), List.of(
                                "Record Type,Error Text",
                                "E,1000: All accounts in the file are invalid"
                        )
                ), Arguments.of("productCodeAndIdBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,VAM_1.0",
                                "2,H,${authlink},1",
                                "3,A,AccName,,,${reference},${secRed}"


                        ), List.of(
                                "Record Type,Error Text",
                                "E,1000: All accounts in the file are invalid",
                                "E,1001: Product Id or Product Code is required"
                        )
                ), Arguments.of("accountNameIsTooShort", "RESPONSE", false, Collections.emptyMap(),
                        List.of(
                                "1,V,VAM_1.0",
                                "2,H,${authlink},1",
                                "3,A,A,${productId},${productCode},${reference},${secRed}"


                        ), List.of(
                                "1,V,VAM_1.0",
                                "2,H,${authlink},1,0",
                                "3,A,FAILED,,,1",
                                "3.01,E,invalid-length-account-name,Name must be between 3 and 250 characters"
                        )
                ), Arguments.of("blankProductCodeAndIncorrectProductId", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,VAM_1.0",
                                "2,H,${authlink},1",
                                "3,A,Anand,,test,${reference},${secRed}"


                        ), List.of(
                                "Record Type,Error Text",
                                "E,1000: All accounts in the file are invalid",
                                "E,1001: Invalid Product Code provided"
                        )
                ), Arguments.of("accountRecordNumberVarchar", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,VAM_1.0",
                                "2,H,${authlink},1",
                                "Three,A,AccName,${productId},${productCode},${reference},${secRed}"


                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 3: Invalid record number Three"
                        )
                ), Arguments.of("headerRecordCountNotMatchingWithRecords", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,VAM_1.0",
                                "2,H,${authlink},2",
                                "3,A,AccName,${productId},${productCode},${reference},${secRed}"


                        ), List.of(
                                "Record Type,Error Text",
                                "E,1000: Row 2: Number of Accounts defined in Header: 2 does not match number of Accounts in file: 1",
                                "E,1001: Account unable to be created due to file-level errors"
                        )
                ), Arguments.of("headerRecordTypeMismatch", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,VAM_1.0",
                                "2,A,${authlink},1",
                                "3,A,AccName,${productId},${productCode},${reference},${secRed}"


                        ), List.of(
                                "Record Type,Error Text",
                                "E,Missing account header record"
                        )
                ), Arguments.of("accountRecordMismatchFields", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,VAM_1.0",
                                "2,H,${authlink},1",
                                "3,A,AccName,${productId},${productCode},orp-85-oskt"


                        ), List.of(
                                "Record Type,Error Text",
                                "E,Missing account header record"
                        )
                ),/* failed Arguments.of("vamAccountOpenRequestWithClientMoneyCustomer", "RESPONSE",false, Collections.emptyMap(),,
                        List.of(
                                "1,V,VAM_1.0",
                                "2,H,${authlink},1",
                                "3,A,AccName,${productId},${productCode},${reference},${secRed}"


                        ), List.of(
                                "Record Type,Error Text",
                                "E,Missing account header record"
                        )
                ),*/Arguments.of("fileNameAuthLinkPresent-NotMatchingWithHeaderCustCode", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,VAM_1.0",
                                "2,H,TEST,1",
                                "3,A,AccName,${productId},${productCode},${reference},${secRed}"


                        ), List.of(
                                "Record Type,Error Text",
                                "E,1000: The Authlink in the file name does not match the Customer Code in the header",
                                "E,1001: Account unable to be created due to file-level errors"
                        )
                ),
                Arguments.of("fileNameAuthLinkNotPresent-NotMatchingWithHeader", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,VAM_1.0",
                                "2,H,TEST,1",
                                "3,A,AccName,${productId},${productCode},${reference},${secRed}"


                        ), List.of(
                                "Record Type,Error Text",
                                "E,1000: The Authlink in the file name does not match the Customer Code in the header",
                                "E,1001: Account unable to be created due to file-level errors"
                        )
                ),
                Arguments.of("vamAccountOpenPositiveFlow", "RESPONSE", false, Collections.emptyMap(),
                        List.of(
                                "1,V,VAM_1.0",
                                "2,H,${authlink},1",
                                "3,A,${accountName},${productId},${productCode},${reference},${secRed}"


                        ), List.of(
                                "1,V,VAM_1.0",
                                "2,H,${authlink},1,0",
                                "3,A,SUCCESS,${accountID},${accountNumber},0"
                        )
                )
        );
    }

    static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of("versionRowMissing", "RESPONSE", false, Map.of(3, List.of(4, 5)),
                        List.of(
                                
                                "2,H,${authlink},1",
                                "3,A,${accountName},${productId},${productCode},${reference},${secRed}"


                        ), List.of(
                                ""
                                )
                )
        );
    }


}
