package com.gresham.bulk.upload.dataprovider;


import org.junit.jupiter.params.provider.Arguments;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class InternalTransferDataProvider {
    static Stream<Arguments> fullSet() {
        return Stream.of(
                Arguments.of("headerRowNumberIsBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(

                                "1,V,1.0",
                                ",H,${authlink},1,11.02,AUD",
                                "3,T,${from},${to},11.02,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: Invalid record number ''"
                        )
                ),Arguments.of("headerRowNumberIsString", "REJECT", false, Collections.emptyMap(),
                        List.of(

                                "1,V,1.0",
                                "one,H,${authlink},1,11.02,AUD",
                                "3,T,${from},${to},11.02,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "Row 2: Invalid record number 'one'"
                        )
                ),Arguments.of("headerRecordTypeIsBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(

                                "1,V,1.0",
                                "2,,${authlink},1,11.02,AUD",
                                "3,T,${from},${to},11.02,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: Unknown record type provided"
                        )
                ),Arguments.of("headerRecordTypeIsNotH", "REJECT", false, Collections.emptyMap(),
                        List.of(

                                "1,V,1.0",
                                "2,A,${authlink},1,11.02,AUD",
                                "3,T,${from},${to},11.02,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: Unknown record type provided"
                        )
                ),Arguments.of("headerCustomerCodeNotMatchingWithAuthlink", "REJECT", false, Collections.emptyMap(),
                        List.of(

                                "1,V,1.0",
                                "2,H,TEST,1,11.02,AUD",
                                "3,T,${from},${to},11.02,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: The Authlink in the file name does not match the Customer Code in the header",
                                "E,Row 3: Internal Transfer unable to be made due to file-level errors"
                        )
                ),Arguments.of("headerCustomerCodeIsBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(

                                "1,V,1.0",
                                "2,H,,1,11.02,AUD",
                                "3,T,${from},${to},11.02,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: missing mandatory field in column 3-customerCode is required-Row 2: The Authlink in the file name does not match the Customer Code in the header",
                                "E,Row 3: Internal Transfer unable to be made due to file-level errors"
                        )
                ),Arguments.of("headerTotalCountIsBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(

                                "1,V,1.0",
                                "2,H,${authlink},,11.02,AUD",
                                "3,T,${from},${to},11.02,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "Row 2: missing mandatory field in column 4-Row 2: The header count was either missing or zero but the file contains 1 detail records",
                                "E,Row 3: Internal Transfer unable to be made due to file-level errors"
                        )
                ),Arguments.of("headerTotalCountIsString", "REJECT", false, Collections.emptyMap(),
                        List.of(

                                "1,V,1.0",
                                "2,H,${authlink},One,11.02,AUD",
                                "3,T,${from},${to},11.02,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: invalid field format 'One' in column 4-Row 2: The header count was either missing or zero but the file contains 1 detail records",
                                "E,Row 3: Internal Transfer unable to be made due to file-level errors"
                        )
                ),Arguments.of("headerTotalCountIsNotMatchingWithTransferRecords", "REJECT", false, Collections.emptyMap(),
                        List.of(

                                "1,V,1.0",
                                "2,H,${authlink},2,11.02,AUD",
                                "3,T,${from},${to},11.02,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: Number of Transfers defined in Header: 2 does not match number of Transfer details in file: 1",
                                "E,Row 3: Internal Transfer unable to be made due to file-level errors"
                        )
                ),Arguments.of("headerTotalAmountIsBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(

                                "1,V,1.0",
                                "2,H,${authlink},1,,AUD",
                                "3,T,${from},${to},11.02,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: missing mandatory field in column 5-Row 2: header totalAmount is invalid-Row 2: The header TotalAmount: 0 does not match the sum of the Detail amounts in the file: 11.02",
                                "E,Row 3: Internal Transfer unable to be made due to file-level errors"
                        )
                ),Arguments.of("headerTotalAmountIsNegative", "REJECT", false, Collections.emptyMap(),
                        List.of(

                                "1,V,1.0",
                                "2,H,${authlink},1,-11.02,AUD",
                                "3,T,${from},${to},-11.02,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: header totalAmount is invalid",
                                "E,Row 3: Internal Transfer unable to be made due to file-level errors"
                        )
                ),Arguments.of("headerTotalAmountIsString", "REJECT", false, Collections.emptyMap(),
                        List.of(

                                "1,V,1.0",
                                "2,H,${authlink},1,One,AUD",
                                "3,T,${from},${to},1.00,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: invalid field format 'One' in column 5-Row 2: header totalAmount is invalid-Row 2: The header TotalAmount: 0 does not match the sum of the Detail amounts in the file: 1.00",
                                "E,Row 3: Internal Transfer unable to be made due to file-level errors"
                        )
                ),Arguments.of("headerTotalAmountIsNotMatchingWithTransferRecord", "REJECT", false, Collections.emptyMap(),
                        List.of(

                                "1,V,1.0",
                                "2,H,${authlink},1,12.02,AUD",
                                "3,T,${from},${to},11.02,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: The header TotalAmount: 12.02 does not match the sum of the Detail amounts in the file: 11.02",
                                "E,Row 3: Internal Transfer unable to be made due to file-level errors"
                        )
                ),Arguments.of("headerCurrencyIsBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(

                                "1,V,1.0",
                                "2,H,${authlink},1,11.02,",
                                "3,T,${from},${to},11.02,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: missing mandatory field in column 6-currency is required",
                                "E,Row 3: Internal Transfer unable to be made due to file-level errors"
                        )
                ),Arguments.of("headerInvalidIsoCurrency", "REJECT", false, Collections.emptyMap(),
                        List.of(

                                "1,V,1.0",
                                "2,H,${authlink},1,11.02,aussie dollar",
                                "3,T,${from},${to},11.02,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "Row 2: header currency is invalid",
                                "E,Row 3: Internal Transfer unable to be made due to file-level errors"
                        )
                ),Arguments.of("headerCurrencyIsNotMatchingWithDetails", "RESPONSE", false, Map.of(3, List.of(3,4,7)),
                        List.of(

                                "1,V,1.0",
                                "2,H,${authlink},1,1.00,NZD",
                                "3,T,${from},${to},1.00,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "1,V,1.0",
                                "2,H,1,0,1",
                                "3,T,024100939,465205314,1.00,AUD,475174,validation_error,Currency code does not match header value,"
                        )
                ),Arguments.of("transferRecordNumberIsBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,1.0",
                                "2,H,${authlink},1,11.00,AUD",
                                ",T,${from},${to},11.00,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 3: Invalid record number ''"
                        )
                ),Arguments.of("transferRecordNumberNotInSequence", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,1.0",
                                "2,H,${authlink},1,11.00,AUD",
                                "99,T,${from},${to},11.00,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 3: Invalid record number '99'"
                        )
                ),Arguments.of("transferRecordNumberIsString", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,1.0",
                                "2,H,${authlink},1,11.00,AUD",
                                "Three,T,${from},${to},11.00,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 3: Invalid record number 'Three'"
                        )
                ),Arguments.of("transferRecordTypeIsBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,1.0",
                                "2,H,${authlink},1,11.00,AUD",
                                "3,,${from},${to},11.00,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 3: Unknown record type provided"
                        )
                ),Arguments.of("transferRecordTypeIsNot'T'", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,1.0",
                                "2,H,${authlink},1,11.00,AUD",
                                "3,Q,${from},${to},11.00,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 3: Unknown record type provided"
                        )
                ),Arguments.of("transferDebitingAccountNumberIsBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,1.0",
                                "2,H,${authlink},1,11.00,AUD",
                                "3,T,,${to},11.00,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,All Transfer details in the file are invalid-Fatal error in transfer detail record(s)",
                                "E,Row 3: missing mandatory field in column 3"
                        )
                ),Arguments.of("transferDebitingAccountNumberIsNotValid", "RESPONSE", false, Map.of(3, List.of(3,4,7)),
                        List.of(
                                "1,V,1.0",
                                "2,H,${authlink},1,11.00,AUD",
                                "3,T,fromAccount,${to},11.00,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "1,V,1.0",
                                "2,H,1,0,1",
                                "3,T,fromAccount,465205314,11.00,AUD,037286,validation_error,Invalid ACMC From Account Number provided,"
                        )
                ),Arguments.of("transferCreditingAccountNumberIsBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,1.0",
                                "2,H,${authlink},1,11.00,AUD",
                                "3,T,${from},,11.00,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,All Transfer details in the file are invalid-Fatal error in transfer detail record(s)",
                                "E,Row 3: missing mandatory field in column 4"

                        )
                ),Arguments.of("transferCreditingAccountNumberIsNotValid", "RESPONSE", false, Map.of(3, List.of(3,4,7)),
                        List.of(
                                "1,V,1.0",
                                "2,H,${authlink},1,11.00,AUD",
                                "3,T,${from},toAccount,11.00,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "1,V,1.0",
                                "2,H,1,0,1",
                                "3,T,024100939,toAccount,11.00,AUD,566922,failed,The account to be credited from unknown,"
                        )
                ),Arguments.of("transferAmountIsBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,1.0",
                                "2,H,${authlink},1,11.00,AUD",
                                "3,T,${from},${to},,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: The header TotalAmount: 11.00 does not match the sum of the Detail amounts in the file: 0",
                                "E,Row 3: missing mandatory field in column 5"

                        )
                ),Arguments.of("transferAmountIsStringValue", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,1.0",
                                "2,H,${authlink},1,11.00,AUD",
                                "3,T,${from},${to},eleven dollar,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: The header TotalAmount: 11.00 does not match the sum of the Detail amounts in the file: 0",
                                "E,Row 3: invalid field format 'eleven dollar' in column 5"

                        )
                ),Arguments.of("transferAmountIsNegativeNumber", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,1.0",
                                "2,H,${authlink},2,11.00,AUD",
                                "3,T,${from},${to},22.00,AUD,${custRef},${payerRef},${payeeRef}",
                                "4,T,${from},${to},-11.00,AUD,${custRef},${payerRef},${payeeRef}"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Fatal error in transfer detail record(s)",
                                "E,Row 4: internal transfer detail amount is invalid"

                        ),Arguments.of("transferCurrencyIsBlank", "REJECT", false, Collections.emptyMap(),
                                List.of(
                                        "1,V,1.0",
                                        "2,H,${authlink},1,9,AUD",
                                        "3,T,${from},${to},9.00,,${custRef},${payerRef},${payeeRef}"


                                ), List.of(
                                        "Record Type,Error Text",
                                        "E,All Transfer details in the file are invalid-Fatal error in transfer detail record(s)",
                                        "E,Row 3: missing mandatory field in column 6-currency is required"

                                )
                        ),Arguments.of("transferCurrencyIsNotIsoValue", "REJECT", false, Collections.emptyMap(),
                                List.of(
                                        "1,V,1.0",
                                        "2,H,${authlink},1,9,AUD",
                                        "3,T,${from},${to},9.00,aussie dollar,${custRef},${payerRef},${payeeRef}"


                                ), List.of(
                                        "Record Type,Error Text",
                                        "E,All Transfer details in the file are invalid-Fatal error in transfer detail record(s)",
                                        "E,Row 3: internal transfer detail currency code is invalid"

                                )
                        ),Arguments.of("transferCurrencyIsNotMatchingWithHeader", "RESPONSE", false, Map.of(3, List.of(3,4,7)),
                                List.of(
                                        "1,V,1.0",
                                        "2,H,${authlink},1,9,AUD",
                                        "3,T,${from},${to},9.00,NZD,${custRef},${payerRef},${payeeRef}"


                                ), List.of(
                                        "1,V,1.0" ,
                                        "2,H,1,0,1",
                                        "3,T,465205314,488091395,9.00,NZD,726013,validation_error,Currency code does not match header value,"

                                )
                        )
                ),Arguments.of("transferReferencesAreBlank", "RESPONSE", false, Map.of(3, List.of(3,4,10)),
                        List.of(
                                "1,V,1.0",
                                "2,H,${authlink},1,9,AUD",
                                "3,T,${from},${to},9.00,AUD,,,"


                        ), List.of(
                                "1,V,1.0",
                                "2,H,1,1,0",
                                "3,T,465205314,488091395,9.00,AUD,,successful,,SW50ZXJuYWxUcmFuc2Zlcjo2NDYwYjdkNDVhY2QzMjg1ODc2MTBiYWYyZWRjMjljMzkwYmIxODEzMjFlYmVjYTMyNTgxMTdiMTEwMmJkMjY3"

                        )
                ),Arguments.of("transferCurrencyAreTooLong", "RESPONSE", false, Map.of(3, List.of(3,4,7)),
                        List.of(
                                "1,V,1.0",
                                "2,H,${authlink},1,9,AUD",
                                "3,T,${from},${to},9.00,AUD,123e4567-e89b-12d3-a456-426614174000,123e4567-e89b-12d3-a456-426614174000,123e4567-e89b-12d3-a456-426614174000"


                        ), List.of(
                                "1,V,1.0",
                                "2,H,1,0,1",
                                "3,T,465205314,488091395,9.00,AUD,123e4567-e89b-12d3-a456-426614174000,failed,Payer reference is limited to 35 characters-Payee reference is limited to 35 characters,"


                        )
                ),Arguments.of("transferCurrencyAreTooLong", "RESPONSE", false, Map.of(3, List.of(3,4)),
                        List.of(
                                "1,V,1.0",
                                "2,H,${authlink},1,9,AUD",
                                "3,T,${from},${to},9.00,AUD,123e4567-e89b-12d3-a456-426614174000-123e4567-e89b-12d3-a456-426614174000-123e4567-e89b-12d3-a456-426614174000-123e4567-e89b-12d3-a456-426614174000,123e4567-e89b-12d3-a456-42661417400,123e4567-e89b-12d3-a456-42661417400"


                        ), List.of(
                                "1,V,1.0",
                                "2,H,1,0,1",
                                "3,T,465205314,488091395,9.00,AUD,123e4567-e89b-12d3-a456-426614174000-123e4567-e89b-12d3-a456-426614174000-123e4567-e89b-12d3-a456-426614174000-123e4567-e89b-12d3-a456-426614174000,failed,Customer transaction reference is limited to 120 characters,"


                        )
                ),Arguments.of("fileNameAuthLinkIsNotValid", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,1.0",
                                "2,H,${authlink},1,9,AUD",
                                "3,T,${from},${to},9.00,AUD,123e4567-e89b-12d3-a456-426614174000-123e4567-e89b-12d3-a456-426614174000-123e4567-e89b-12d3-a456-426614174000-123e4567-e89b-12d3-a456-426614174000,123e4567-e89b-12d3-a456-42661417400,123e4567-e89b-12d3-a456-42661417400"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: The Authlink in the file name does not match the Customer Code in the header-Row 2: Unable to retrieve Customer record for AuthLink in file name",
                                "E,Row 3: Internal Transfer unable to be made due to file-level errors"
                        )
                ),Arguments.of("fileNameAuthLinkIsDifferentThanDataInFile", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,1.0",
                                "2,H,${authlink},1,9,AUD",
                                "3,T,${from},${to},9.00,AUD,123e4567-e89b-12d3-a456-426614174000-123e4567-e89b-12d3-a456-426614174000-123e4567-e89b-12d3-a456-426614174000-123e4567-e89b-12d3-a456-426614174000,123e4567-e89b-12d3-a456-42661417400,123e4567-e89b-12d3-a456-42661417400"
                        ), List.of(
                                "Record Type,Error Text",
                                "Row 2: The Authlink in the file name does not match the Customer Code in the header",
                                "E,Row 3: Internal Transfer unable to be made due to file-level errors"
                        )
                )
        );
    }
    static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of("fileNameAuthLinkIsDifferentThanDataInFile", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,1.0",
                                "2,H,${authlink},1,9,AUD",
                                "3,T,${from},${to},9.00,AUD,123e4567-e89b-12d3-a456-426614174000-123e4567-e89b-12d3-a456-426614174000-123e4567-e89b-12d3-a456-426614174000-123e4567-e89b-12d3-a456-426614174000,123e4567-e89b-12d3-a456-42661417400,123e4567-e89b-12d3-a456-42661417400"
                        ), List.of(
                                "Record Type,Error Text", 
                                "Row 2: The Authlink in the file name does not match the Customer Code in the header", 
                                "E,Row 3: Internal Transfer unable to be made due to file-level errors"
                        )
                )
        );
    }



}
