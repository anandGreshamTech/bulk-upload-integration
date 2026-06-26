package com.gresham.bulk.upload.dataprovider;


import org.junit.jupiter.params.provider.Arguments;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class AggregateReceiptDataProvider {
     static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of("happyPath", "RESPONSE","claName"
                        ,false,Map.of(2,List.of(3,10)),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,${claName},,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracing"
    
                        ), List.of(
                                "1,V,2.0", "2,H,QWdncmVnYXRlUGF5bWVudDoyNGU4MWNlMGViMjA4OGY1YzUzZjYxYzJmMDJiYjI1NzFkMzk5MjQ3NzAzNTdiNjg5NDZiZmEwMWMzY2NmMThj,1,1,0,2.92,2.92,0.00,sjz-53-pguj,Automation,SUCCESS,SUCCESS,"
                        )
                ),Arguments.of("invalidHeaderRecordType", "REJECT","claName"
                        ,false,Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,A,${authlink},1,Automation-2.92,Automation,2.92,AUD,CREDIT,,${claName},,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: Unknown record type provided"

                        )
                ),Arguments.of("emptyHeaderRecordType", "REJECT","claName",false,Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,,${authlink},1,Automation-2.92,Automation,2.92,AUD,CREDIT,,${claName},,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: Unknown record type provided"

                        )
                ),
                Arguments.of("V2 error 'emptyCustomerCode", "REJECT","claName",false,Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,,1,Automation-2.92,Automation,2.92,AUD,CREDIT,,${claName},,,,,,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: missing mandatory field in column 2-Row 1: The Authlink in the file name does not match the Customer Code in the header-customerCode is required",
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"

                        )
                ),
                Arguments.of("customerNameMismatch", "REJECT","claName",false,Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,TTT,1,Automation-2.92,Automation,2.92,AUD,CREDIT,,${claName},,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text", 
                                "E,Row 2: The Authlink in the file name does not match the Customer Code in the header", 
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"

                        )
                ),
                Arguments.of("countMismatch", "REJECT","claName",false,Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},2,Automation-2.92,Automation,2.92,AUD,CREDIT,,${claName},,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text", 
                                "E,Row 2: Number of Instructions defined in Header: 2 does not match number of Instruction details in file: 1", 
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"

                        )
                ),
                Arguments.of("countBlack", "REJECT","claName",false,Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},,Automation-2.92,Automation,2.92,AUD,CREDIT,,${claName},,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text", 
                                "E,Row 2: missing mandatory field in column 4-Row 2: The header count was either missing or zero but the file contains 1 Instruction records", 
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"

                        )
                ),
                Arguments.of("countIsChar", "REJECT","claName",false,Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},one,Automation-2.92,Automation,2.92,AUD,CREDIT,,${claName},,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text", 
                                "E,Row 2: invalid field format 'one' in column 4-Row 2: The header count was either missing or zero but the file contains 1 Instruction records", 
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"

                        )
                ),
                Arguments.of("countNegative", "REJECT","claName",false,Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},-1,Automation-2.92,Automation,2.92,AUD,CREDIT,,${claName},,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text", 
                                "E,Row 2: Number of Instructions defined in Header: -1 does not match number of Instruction details in file: 1", 
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"

                        )
                ),
                Arguments.of("referenceIsBlank", "REJECT","claName",false,Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,,Automation,2.92,AUD,CREDIT,,${claName},,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text", 
                                "E,Row 2: missing mandatory field in column 5-reference is required", 
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"

                        )
                ),Arguments.of("totalIsBlank", "REJECT","claName",false,Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,Automation-2.92,Automation,,AUD,CREDIT,,${claName},,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: missing mandatory field in column 7-Row 2: Header totalAmount is invalid-Row 2: The header TotalAmount: 0 does not match the sum of the Instruction amounts in the file: 2.92", 
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"

                        )
                ),Arguments.of("totalMismatch", "REJECT","claName",false,Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,Automation-2.92,Automation,2.90,AUD,CREDIT,,${claName},,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: The header TotalAmount: 2.90 does not match the sum of the Instruction amounts in the file: 2.92",
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"

                        )
                ),Arguments.of("headerCurrencyBlank", "REJECT","claName",false,Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,Automation-2.92,Automation,2.92,,CREDIT,,${claName},,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text", 
                                "E,Row 2: missing mandatory field in column 8-currency is required", 
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"

                        )
                ),Arguments.of("headerCurrencyMismatch", "REJECT","claName",false,Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,Automation-2.92,Automation,2.92,australian doller,CREDIT,,${claName},,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text", 
                                "E,Row 2: Header currency is invalid", 
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"

                        )
                ),Arguments.of("headerInstructionCurrencyMismatch", "RESPONSE","claName"
                        ,false, Map.of(3,List.of(3)),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,Automation-2.92,Automation,2.92,INR,CREDIT,,${claName},,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "1,V,2.0", 
                                "2,H,,1,0,1,2.92,0,2.92,Automation-2.92,Automation,COMMAND_FAILURE,COMMAND_FAILURE,Instructions currency does not match overall currency-Tracing ID exceeds maximum length of 50 characters", 
                                "3,I,QWNjb3VudDozMDExYmYwMzFkN2EyMGU0ZGRiMzQzYTFjY2NmMjFlOTE5ZWFkMDZhZDMzZWI4NTM1MDE5M2RhNDE5NGI0YjEy,000010013,2.92,AUD,CREDIT,narrative,tracingId for 2.9156976,FAILURE,Payment request failure"

                        )

                ),
                Arguments.of("headerTypeIsBlank", "REJECT","claName"
                        ,false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,Automation-2.92,Automation,2.92,AUD,,,${claName},,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text", 
                                "E,Row 2: missing mandatory field in column 9-credit debit type is required-Row 2: Header Invalid debit/credit type provided for an Aggregate Receipt file", 
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"

                        )

                ),Arguments.of("headerTypeIsIncorrect", "REJECT","claName"
                        ,false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,Automation-2.92,Automation,2.92,AUD,DEBI,,${claName},,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text", 
                                "E,Row 2: Header Invalid debit/credit type provided for an Aggregate Receipt file", 
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"
                        )

                ),Arguments.of("headerTypeIsNotDebitOrCredit", "REJECT","claName"
                        ,false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,Automation-2.92,Automation,2.92,AUD,TEST,,${claName},,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text", 
                                "E,Row 2: Header Invalid debit/credit type provided for an Aggregate Receipt file", 
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"
                        )

                ),Arguments.of("headerClaAndBankDetailsBlank", "REJECT","claName"
                        ,false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,Automation-2.92,Automation,2.92,AUD,CREDIT,,,,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text", 
                                "E,Row 2: Header CLA required for Credit payment types", 
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"
                        )

                ),Arguments.of("headerOnlyExternalBankCodeProvided", "REJECT","claName"
                        ,false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,Automation-2.92,Automation,2.92,AUD,CREDIT,,,234324,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text", 
                                "E,Row 2: Header External Account Number should not be provided for CLA payments-Row 2: Header CLA required for Credit payment types", 
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"
                        )

                ),Arguments.of("headerOnlyExternalAccountNumberProvided", "REJECT","claName"
                        ,false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,Automation-2.92,Automation,2.92,AUD,CREDIT,,,23476567,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: Header External Account Number should not be provided for CLA payments-Row 2: Header CLA required for Credit payment types",
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"
                        )

                ),Arguments.of("headerExternalAccountNumberAndBsbProvided", "REJECT","claName"
                        ,false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,Automation-2.92,Automation,2.92,AUD,CREDIT,,,23476567,333244,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text", 
                                "E,Row 2: Header External Account Number should not be provided for CLA payments-Row 2: Header External Bank Code should not be provided for CLA payments-Row 2: Header CLA required for Credit payment types", 
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"                       
                        )

                ),Arguments.of("nonClaPayment-PaymentTypeMissing", "REJECT","claName"
                        ,false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,Automation-2.92,Automation,2.92,AUD,CREDIT,,,23476567,333244,TEST user,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text", 
                                "E,Row 2: Header External Account Number should not be provided for CLA payments-Row 2: Header External Bank Code should not be provided for CLA payments-Row 2: Header External Name should not be provided for CLA payments-Row 2: Header CLA required for Credit payment types", 
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"
                        )

                ),Arguments.of("nonClaPayment-PaymentTypeIncorrect", "REJECT","claName"
                        ,false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,Automation-2.92,Automation,2.92,AUD,CREDIT,,,23476567,333244,TEST user,TEST,,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text", 
                                "E,Row 2: Header Payment Type should not be provided for CLA payments-Row 2: Header CLA required for Credit payment types", 
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"
                        )

                ),Arguments.of("rec-with-DE", "REJECT",""
                        ,false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,,23476567,333244,TEST user,DE,,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: Header Payment Type should not be provided for CLA payments-Row 2: Header CLA required for Credit payment types",
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"
                        )

                ),Arguments.of("rec-with-RTGS", "REJECT",""
                        ,false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,,23476567,333244,TEST user,RTGS,sydney,,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: Header Payment Type should not be provided for CLA payments-Row 2: Header CLA required for Credit payment types",
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"                        )

                ),Arguments.of("rec-RTGS-payment", "REJECT",""
                        ,false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,,23476567,333244,TEST user,RTGS,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Error Text", 
                                "E,Row 2: Header Payment Type should not be provided for CLA payments-Row 2: Header CLA required for Credit payment types", 
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"
                        )
                ),Arguments.of("ClaAndExternalAccountDetailsPresent", "REJECT",""
                        ,false,Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,${claName},23476567,333244,TEST user,DE,,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [paymentType should not be provided for CLA payments]"
                        )
                ),Arguments.of("ClaAndExternalAccountDetailsExceptPaymentTypePresent", "REJECT",""
                        ,false, Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,${claId},${claName},23476567,333244,TEST user,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,\"Row 1: [externalAccountNumber should not be provided for CLA payments, externalBankCode should not be provided for CLA payments, externalName should not be provided for CLA payments]\""
                        )
                ),Arguments.of("ClaIdAndClaNameBothValid", "RESPONSE","both"
                        ,false, Map.of(2,List.of(2,9)),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,${claId},${claName},,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Id,Total Count,Successful Count,Failed Count,Total Amount,Successful Amount,Failed Amount,Reference,Customer Reference,Payment State,Payment Status,Payment Reason",
                                "2,H,QWdncmVnYXRlUGF5bWVudDoyZjlkZDk2NGRhZjFlOTFlMzk2NDAzZmY0MTk1Njg0MjdmMDJmOTdiNDA1NDU3NmJkMDljNWYyODE2MDQxZjY0,1,1,0,2.92,2.92,0.00,usz-50-ppyc,Automation,SUCCESS,SUCCESS,"
                        )
                ),Arguments.of("ClaNameInvalid", "REJECT","claId"
                        ,false,Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,${claId},claName,,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: Header CLA Id provided does not match CLA Id for Account found using CLA Name provided",
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"
                        )
                ),Arguments.of("ClaIdInvalid", "REJECT","claName"
                        ,false,Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,cla,${claName},,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: Header CLA Id provided does not match CLA Id for Account found using CLA Name provided",
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"
                        )
                ),Arguments.of("ClaNameInvalid", "REJECT","claId"
                        ,false,Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,${claId},claName,,,,,sydney,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: Invalid header CLA Name provided",
                                "E,Row 3: Aggregate Payment unable to be made due to file-level errors"
                        )
                )
                
                );
    }
     static Stream<Arguments> dataBackup() {
        return Stream.of(
                Arguments.of("happyPath", "RESPONSE","claName"
                        ,false,Map.of(2,List.of(2,9)),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,,233232233,232323,account-name,RTGS,Town,",
                                "3,I,,${account},2.92,AUD,CREDIT,narrative,T"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,\"Row 1: [The AuthLink in the file name does not match the Customer Code in the header, Unable to retrieve Customer record for AuthLink in file name]\"",
                                "E,Row 2: null"
                        )
                )
        );
    }


}
