package com.gresham.bulk.upload.dataprovider;


import org.junit.jupiter.params.provider.Arguments;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class AggregateReceiptDataProvider {
    static Stream<Arguments> dataFull() {
        return Stream.of(
                Arguments.of("happyPath----raise issue", "RESPONSE", "claName"
                        , false, Map.of(2, List.of(2, 9)),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,Automation-2.92,Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,\"Row 1: [The AuthLink in the file name does not match the Customer Code in the header, Unable to retrieve Customer record for AuthLink in file name]\"",
                                "E,Row 2: null"
                        )
                ), Arguments.of("invalidHeaderRecordType", "REJECT", "claName"
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "A,${authlink},1,Automation-2.92,Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: Unknown record type provided"

                        )
                ), Arguments.of("emptyHeaderRecordType", "REJECT", "claName", false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                ",${authlink},1,Automation-2.92,Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: Unknown record type provided"

                        )
                ),
                Arguments.of("emptyCustomerName", "REJECT", "claName", false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,,1,Automation-2.92,Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: missing mandatory field in column 2-Row 1: The Authlink in the file name does not match the Customer Code in the header-customerCode is required",
                                "E,Row 2: Aggregate Payment unable to be made due to file-level errors"

                        )
                ),
                Arguments.of("customerNameMismatch", "REJECT", "claName", false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,TTT,1,Automation-2.92,Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [The AuthLink in the file name does not match the Customer Code in the header]"

                        )
                ),
                Arguments.of("countMismatch", "REJECT", "claName", false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},2,Automation-2.92,Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [The header Instruction Count does not match the number of Instructions in the file]"

                        )
                ),
                Arguments.of("countBlack", "REJECT", "claName", false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},,Automation-2.92,Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,\"Row 1: [recordCount is required, Invalid value provided for Instruction Count]\""

                        )
                ),
                Arguments.of("countIsChar", "REJECT", "claName", false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},one,Automation-2.92,Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [Invalid value provided for Instruction Count]"

                        )
                ),
                Arguments.of("countNegative", "REJECT", "claName", false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},-1,Automation-2.92,Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [Invalid value provided for Instruction Count]"

                        )
                ),
                Arguments.of("referenceIsBlank", "REJECT", "claName", false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,,Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [reference is required]"

                        )
                ), Arguments.of("totalIsBlank", "REJECT", "claName", false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,Automation-2.92,Automation,,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,\"Row 1: [totalAmount is required, totalAmount is invalid][The header TotalAmount does not match the sum of the Instruction amounts in the file]\"]"

                        )
                ), Arguments.of("totalMismatch", "REJECT", "claName", false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,Automation-2.92,Automation,2.90,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: The header TotalAmount: 2.90 does not match the sum of the Instruction amounts in the file: 2.92",
                                "E,Row 2: Aggregate Payment unable to be made due to file-level errors"

                        )
                ), Arguments.of("headerCurrencyBlank", "REJECT", "claName", false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,Automation-2.92,Automation,2.92,,DEBIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [currency is required]"

                        )
                ), Arguments.of("headerCurrencyMismatch", "REJECT", "claName", false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,Automation-2.92,Automation,2.92,australian doller,DEBIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [header currency is invalid]"

                        )
                ), Arguments.of("headerInstructionCurrencyMismatch", "RESPONSE", "claName"
                        , false, Map.of(3, List.of(3)),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,Automation-2.92,Automation,2.92,INR,DEBIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Id,Total Count,Successful Count,Failed Count,Total Amount,Successful Amount,Failed Amount,Reference,Customer Reference,Payment State,Payment Status,Payment Reason",
                                "H,,1,0,1,2.92,0,2.92,Automation-2.92,Automation,COMMAND_FAILURE,COMMAND_FAILURE,Instructions currency does not match overall currencyTracing ID exceeds maximum length of 50 characters",
                                "I,2,QWNjb3VudDo4MGI1YWVjYzZkYWE5NzdlZTIyNGQyNGQ0ZDFkZTgwZTZlNTNlYjg1NTUwM2RkOTI2NmE0ZDI3MTBhZDM3OGU3,398335559,2.92,AUD,CREDIT,narrative,tracingId for 2.9156976,FAILURE,Payment request failure"

                        )

                ),
                Arguments.of("headerTypeIsBlank", "REJECT", "claName"
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,Automation-2.92,Automation,2.92,AUD,,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,\"Row 1: [credit debit type is required, Invalid debit/credit type provided for an Aggregate Payment file]\""

                        )

                ), Arguments.of("headerTypeIsIncorrect", "REJECT", "claName"
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,Automation-2.92,Automation,2.92,AUD,DEBI,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [Invalid debit/credit type provided for an Aggregate Payment file]"
                        )

                ), Arguments.of("headerTypeIsNotDebitOrCredit", "REJECT", "claName"
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,Automation-2.92,Automation,2.92,AUD,TEST,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [Invalid debit/credit type provided for an Aggregate Payment file]"
                        )

                ), Arguments.of("headerClaAndBankDetailsBlank", "REJECT", "claName"
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,Automation-2.92,Automation,2.92,AUD,CREDIT,,,,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,\"Row 1: [external payment type is required for non-CLA payments, invalid payment type provided, externalBankCode is required, externalAccountNumber is required, externalName is required]\""
                        )

                ), Arguments.of("headerOnlyExternalBankCodeProvided", "REJECT", "claName"
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,Automation-2.92,Automation,2.92,AUD,CREDIT,,,234324,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,\"Row 1: [external payment type is required for non-CLA payments, invalid payment type provided, externalBankCode is required, externalName is required]\""
                        )

                ), Arguments.of("headerOnlyExternalAccountNumberProvided", "REJECT", "claName"
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,Automation-2.92,Automation,2.92,AUD,CREDIT,,,23476567,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,\"Row 1: [external payment type is required for non-CLA payments, invalid payment type provided, externalBankCode is required, externalName is required]\"]"
                        )

                ), Arguments.of("headerExternalAccountNumberAndBsbProvided", "REJECT", "claName"
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,Automation-2.92,Automation,2.92,AUD,CREDIT,,,23476567,333244,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,\"Row 1: [external payment type is required for non-CLA payments, invalid payment type provided, externalName is required]\""
                        )

                ), Arguments.of("nonClaPayment-PaymentTypeMissing", "REJECT", "claName"
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,Automation-2.92,Automation,2.92,AUD,CREDIT,,,23476567,333244,TEST user,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,\"Row 1: [external payment type is required for non-CLA payments, invalid payment type provided]\""
                        )

                ), Arguments.of("nonClaPayment-PaymentTypeIncorrect", "REJECT", "claName"
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,Automation-2.92,Automation,2.92,AUD,CREDIT,,,23476567,333244,TEST user,TEST",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [invalid payment type provided]"
                        )

                ), Arguments.of("nonClaPayment-DE-InsufficientFunds", "RESPONSE", ""
                        , false, Map.of(2, List.of(2, 9), 3, List.of(3, 4)),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,,23476567,333244,TEST user,DE",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Id,Total Count,Successful Count,Failed Count,Total Amount,Successful Amount,Failed Amount,Reference,Customer Reference,Payment State,Payment Status,Payment Reason",
                                "H,QWdncmVnYXRlUGF5bWVudDphNDU4ZjY3OTYwOTFlYWIxMjJlYWIxMmE3OWU1YzUwNjhlYTUyMTFhZDBhODZlNTJlZTk0MDhmMTU0YzVkMmVm,1,0,1,2.92,0.00,0.00,Automation-2.92,Automation,PAYMENT_FAILURE,PAYMENT_FAILURE,Please contact the bank aggregate-payment|All instructions have failed",
                                "I,2,QWNjb3VudDo4MGI1YWVjYzZkYWE5NzdlZTIyNGQyNGQ0ZDFkZTgwZTZlNTNlYjg1NTUwM2RkOTI2NmE0ZDI3MTBhZDM3OGU3,398335559,2.92,AUD,CREDIT,narrative,2.92,failed,amount|Insufficient funds"
                        )

                ), Arguments.of("nonClaPayment-RTGS-InsufficientFunds", "RESPONSE", ""
                        , false, Map.of(2, List.of(2, 9), 3, List.of(3, 4)),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,,23476567,333244,TEST user,RTGS",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Id,Total Count,Successful Count,Failed Count,Total Amount,Successful Amount,Failed Amount,Reference,Customer Reference,Payment State,Payment Status,Payment Reason",
                                "H,QWdncmVnYXRlUGF5bWVudDphNDU4ZjY3OTYwOTFlYWIxMjJlYWIxMmE3OWU1YzUwNjhlYTUyMTFhZDBhODZlNTJlZTk0MDhmMTU0YzVkMmVm,1,0,1,2.92,0.00,0.00,Automation-2.92,Automation,PAYMENT_FAILURE,PAYMENT_FAILURE,Please contact the bank aggregate-payment|All instructions have failed",
                                "I,2,QWNjb3VudDo4MGI1YWVjYzZkYWE5NzdlZTIyNGQyNGQ0ZDFkZTgwZTZlNTNlYjg1NTUwM2RkOTI2NmE0ZDI3MTBhZDM3OGU3,398335559,2.92,AUD,CREDIT,narrative,2.92,failed,amount|Insufficient funds"
                        )

                ), Arguments.of("nonClaPayment-RTGS-payment", "RESPONSE", ""
                        , false, Map.of(2, List.of(2, 9)),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,,23476567,333244,TEST user,RTGS",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Id,Total Count,Successful Count,Failed Count,Total Amount,Successful Amount,Failed Amount,Reference,Customer Reference,Payment State,Payment Status,Payment Reason",
                                "H,QWdncmVnYXRlUGF5bWVudDoxYmIwNzgyNzQ5ODYzN2JkNWJmYTY3ZTU4NmU0YTZkOTA2YWYxYjk5YjNlMWQ5ZTFlMjIzMWRiYWEyOTBlYzk4,1,1,0,2.92,2.92,0.00,dzl-05-ibbr,Automation,SUCCESS,SUCCESS,"
                        )
                ), Arguments.of("ClaAndExternalAccountDetailsPresent", "REJECT", ""
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,${claName},23476567,333244,TEST user,DE",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [paymentType should not be provided for CLA payments]"
                        )
                ), Arguments.of("ClaAndExternalAccountDetailsExceptPaymentTypePresent", "REJECT", ""
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,${claId},${claName},23476567,333244,TEST user,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,\"Row 1: [externalAccountNumber should not be provided for CLA payments, externalBankCode should not be provided for CLA payments, externalName should not be provided for CLA payments]\""
                        )
                ), Arguments.of("ClaIdAndClaNameBothValid", "RESPONSE", "both"
                        , false, Map.of(2, List.of(2, 9)),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,${claId},${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Id,Total Count,Successful Count,Failed Count,Total Amount,Successful Amount,Failed Amount,Reference,Customer Reference,Payment State,Payment Status,Payment Reason",
                                "H,QWdncmVnYXRlUGF5bWVudDoyZjlkZDk2NGRhZjFlOTFlMzk2NDAzZmY0MTk1Njg0MjdmMDJmOTdiNDA1NDU3NmJkMDljNWYyODE2MDQxZjY0,1,1,0,2.92,2.92,0.00,usz-50-ppyc,Automation,SUCCESS,SUCCESS,"
                        )
                ), Arguments.of("ClaNameInvalid", "REJECT", "claId"
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,${claId},claName,,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: Header CLA Id provided does not match CLA Id for Account found using CLA Name provided",
                                "E,Row 2: Aggregate Payment unable to be made due to file-level errors"
                        )
                ), Arguments.of("ClaIdInvalid", "REJECT", "claName"
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,cla,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: Header CLA Id provided does not match CLA Id for Account found using CLA Name provided",
                                "E,Row 2: Aggregate Payment unable to be made due to file-level errors"
                        )
                ), Arguments.of("ClaNameInvalid", "REJECT", "claId"
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,${claId},claName,,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: Invalid header CLA Name provided",
                                "E,Row 2: Aggregate Payment unable to be made due to file-level errors"
                        )
                ), Arguments.of("instructionRecordTypeIsNotI", "REJECT", "claName"
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "J,2,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: Unknown record type provided"
                        )
                ), Arguments.of("instructionRecordTypeIsBlank", "REJECT", "claName"
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                ",2,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: Unknown record type provided"
                        )
                ), Arguments.of("instructionRecordNumberStartsWith3", "REJECT", "claName"
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,3,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: Invalid record number 3"
                        )
                ), Arguments.of("instructionRecordNumberIsBlank", "REJECT", "claName"
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,,,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: Invalid record number "
                        )
                ), Arguments.of("instructionAccountIdIsNotCorrect", "RESPONSE", "claName"
                        , false, Map.of(2, List.of(2, 9), 3, List.of(3, 4)),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,accId,${account},2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Id,Total Count,Successful Count,Failed Count,Total Amount,Successful Amount,Failed Amount,Reference,Customer Reference,Payment State,Payment Status,Payment Reason",
                                "H,,1,0,1,2.92,0,2.92,her-52-annr,Automation,COMMAND_FAILURE,COMMAND_FAILURE,All accounts do not exist",
                                "I,2,000010013,000010013,2.92,AUD,CREDIT,narrative,2.92,FAILURE,Payment request failure"

                        )
                ), Arguments.of("instructionAccountNumberIsWrong", "RESPONSE", "claName"
                        , false, Map.of(2, List.of(2, 9), 3, List.of(3, 4)),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,,account,2.92,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Id,Total Count,Successful Count,Failed Count,Total Amount,Successful Amount,Failed Amount,Reference,Customer Reference,Payment State,Payment Status,Payment Reason",
                                "H,,1,0,1,2.92,0,2.92,her-52-annr,Automation,COMMAND_FAILURE,COMMAND_FAILURE,All accounts do not exist",
                                "I,2,000010013,000010013,2.92,AUD,CREDIT,narrative,2.92,FAILURE,Payment request failure"

                        )
                ), Arguments.of("instructionAmountIsBlank----raise issue", "REJECT", "claName"
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "",
                                ""

                        )
                ), Arguments.of("instructionAmountNotMatchingWithHeader", "REJECT", "claName"
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},2.93,AUD,CREDIT,narrative,2.92"

                        ), List.of(
                                "",
                                ""

                        )
                ), Arguments.of("instructionCurrencyIsNotMatchingWithHeader", "RESPONSE", "claName"
                        , false, Map.of(2, List.of(2, 9), 3, List.of(3, 4)),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},2.92,NZD,CREDIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Id,Total Count,Successful Count,Failed Count,Total Amount,Successful Amount,Failed Amount,Reference,Customer Reference,Payment State,Payment Status,Payment Reason",
                                "H,,1,0,1,2.92,0,2.92,eyc-25-hofj,Automation,COMMAND_FAILURE,COMMAND_FAILURE,Instructions currency does not match overall currency",
                                "I,2,QWNjb3VudDozMDExYmYwMzFkN2EyMGU0ZGRiMzQzYTFjY2NmMjFlOTE5ZWFkMDZhZDMzZWI4NTM1MDE5M2RhNDE5NGI0YjEy,000010013,2.92,NZD,CREDIT,narrative,2.92,FAILURE,Payment request failure"
                        )
                ),Arguments.of("instructionCurrencyIsBlank", "REJECT", "claName"
                        , false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},2.92,,CREDIT,narrative,2.92"

                        ), List.of(
                                "",
                                ""
                        )
                ),Arguments.of("instructionTypeIsNotCreditForReceipt", "RESPONSE", "claName"
                        , false,Map.of(2, List.of(2, 9), 3, List.of(3, 4)),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,DEBIT,narrative,2.92"

                        ), List.of(
                                "Record Type,Id,Total Count,Successful Count,Failed Count,Total Amount,Successful Amount,Failed Amount,Reference,Customer Reference,Payment State,Payment Status,Payment Reason",
                                "H,,1,0,1,2.92,0,2.92,vpc-90-xpdr,Automation,COMMAND_FAILURE,COMMAND_FAILURE,Instructions value does not add up to overall value",
                                "I,2,QWNjb3VudDozMDExYmYwMzFkN2EyMGU0ZGRiMzQzYTFjY2NmMjFlOTE5ZWFkMDZhZDMzZWI4NTM1MDE5M2RhNDE5NGI0YjEy,000010013,2.92,AUD,DEBIT,narrative,2.92,FAILURE,Payment request failure"
                        )
                ),Arguments.of("instructionTypeIsBlank", "REJECT", "claName"
                        , false,Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,,narrative,tid"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 3: missing mandatory field in column 7-credit debit type is required"
                        )
                ),Arguments.of("instructionTypeIsTest", "REJECT", "claName"
                        , false,Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,TEST,narrative,tid"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 3: missing mandatory field in column 7-credit debit type is required"
                        )
                ),Arguments.of("instructionNarrativeIsBlank", "REJECT", "claName"
                        , false,Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,,tid"

                        ), List.of(
                                "Record Type,Error Text",
                                ""
                        )
                ),Arguments.of("instructionNarrativeIsVeryLong", "RESPONSE", "claName"
                        , false,Map.of(2, List.of(2, 9), 3, List.of(3, 4)),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,f8ef503e-cffb-2e93-8f16-0c847fc0ea26-e92b91b930cb-58dcbdcf87a2-90b9eafdc030-7ff63d8f1af9-5308ab084fc9-e185ff3ec33d-3079d5fa5893-02a722ed461d-f370d67c6ddb-e2f7bc367872-df321912723e-33c4e12761f4-626fdac,tid"

                        ), List.of(
                                "Record Type,Id,Total Count,Successful Count,Failed Count,Total Amount,Successful Amount,Failed Amount,Reference,Customer Reference,Payment State,Payment Status,Payment Reason",
                                "H,,1,0,1,2.92,0,2.92,yze-84-ouuo,Automation,COMMAND_FAILURE,COMMAND_FAILURE,Narrative must be 140 characters or less for payments",
                                "I,2,QWNjb3VudDozMDExYmYwMzFkN2EyMGU0ZGRiMzQzYTFjY2NmMjFlOTE5ZWFkMDZhZDMzZWI4NTM1MDE5M2RhNDE5NGI0YjEy,000010013,2.92,AUD,CREDIT,f8ef503e-cffb-2e93-8f16-0c847fc0ea26-e92b91b930cb-58dcbdcf87a2-90b9eafdc030-7ff63d8f1af9-5308ab084fc9-e185ff3ec33d-3079d5fa5893-02a722ed461d-f370d67c6ddb-e2f7bc367872-df321912723e-33c4e12761f4-626fdac,tid,FAILURE,Payment request failure"

                        )
                ),
                Arguments.of("instructionTracingIdIsBlank", "REJECT", "claName"
                        , false,Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,${reference},Automation,2.92,AUD,CREDIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,CREDIT,narrative,"

                        ), List.of(
                                ""

                        ),Arguments.of("instruction", "REJECT", "claName"
                                , false,Collections.emptyMap(),
                                List.of(
                                        "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                        "H,${authlink},2,${reference},Automation,3.92,AUD,CREDIT,,${claName},,,,",
                                        "I,2,,${account},2.92,AUD,CREDIT,narrative,tid-1",
                                        "I,3,,${account},1,AUD,CREDIT,narrative,"

                                ), List.of(
                                        ""

                                )
                        )
                )


        );
    }

    static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of("totalMismatch", "REJECT", "claName", false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,Automation-2.92,Automation,2.92,AUD,DEBIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,TEST,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: The header TotalAmount: 2.90 does not match the sum of the Instruction amounts in the file: 2.92",
                                "E,Row 2: Aggregate Payment unable to be made due to file-level errors"

                        )
                )
        );
    }


}
