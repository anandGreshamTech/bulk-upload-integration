package com.gresham.bulk.upload.dataprovider;


import org.junit.jupiter.params.provider.Arguments;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class PaymentDataProvider {
    static Stream<Arguments> dataFullSet() {
        return Stream.of(
                Arguments.of("invalidHeaderIdentifier", "REJECT",
                        false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "He,${authlink},1,7.69,AUD",
                                "P,2,,${account},7.69,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [header recordType is invalid]"
                        )
                ),Arguments.of("currencyRequired", "REJECT",false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.69,",
                                "P,2,,${account},7.69,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [currency is required]",
                                "E,Row 2: null"
                        )
                ),
                Arguments.of("currencyInvalid", "REJECT",false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.69,aussiDoller",
                                "P,2,,${account},7.69,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [header currency is invalid]",
                                "E,Row 2: null"
                        )
                ),Arguments.of("currencyMismatchWithPaymentRecord", "RESPONSE",false, Map.of(3,List.of(3,4)),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.69,INR",
                                "P,2,,${account},7.69,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"

                        ), List.of(
                                "Record Type,Total Count,Successful Count,Failed Count",
                                "H,1,0,1",
                                "P,2,accountId,accountNumber,7.69,AUD,rtgs,RTGS Automation,,PAYMENT_FAILURE,[currency code does not match header value],PAYMENT_FAILURE"
                        )
                ),
                Arguments.of("customerCodeRequired", "REJECT",false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,,1,7.69,AUD",
                                "P,2,,${account},7.69,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [customerCode is required][The AuthLink in the file name does not match the Customer Code in the header]",
                                "E,Row 2: File validation failed"
                        )
                ),Arguments.of("HeaderIndentifierBlank", "REJECT",false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                ",${authlink},1,7.69,AUD",
                                "P,2,,${account},7.69,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,\"Row 1: [recordType is required, header recordType is invalid]\"",
                                "E,Row 2: File validation failed"
                        )
                ),Arguments.of("incorrectAuthlinkInHeader", "REJECT",false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,Test,1,7.69,AUD",
                                "P,2,,${account},7.69,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [The AuthLink in the file name does not match the Customer Code in the header]",
                                "E,Row 2: File validation failed"
                        )
                ),Arguments.of("incorrectAuthlinkInFileName", "REJECT",false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.69,AUD",
                                "P,2,,${account},7.69,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: The Authlink in the file name does not match the Customer Code in the header-Row 1: Unable to retrieve Customer record for AuthLink in file name"

                        )
                ),Arguments.of("TotalCountIsBlank --BUG--line numbers are wrong", "REJECT",false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},,7.69,AUD",
                                "P,2,,${account},7.69,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: missing mandatory field in column 3-Row 1: The header count was either missing or zero but the file contains 1 detail records",
                                "E,Row 2: Payment unable to be made due to file-level errors"


                        )
                ),Arguments.of("TotalCountMismatchWithPaymentRecordCount", "REJECT",false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},2,7.69,AUD",
                                "P,2,,${account},7.69,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"

                        ), List.of(
                                "Record Type,Error Text",
                                "Row 1: Number of Payments defined in Header: 2 does not match number of Payment details in file: 1",
                                "E,Row 2: Payment unable to be made due to file-level errors"


                        )
                ),                Arguments.of("TotalCountIsString-- bug error message is wrong", "REJECT",false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},One,7.69,AUD",
                                "P,2,,${account},7.69,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: invalid field format 'One' in column 3-Row 1: The header count was either missing or zero but the file contains 1 detail records",
                                "E,Row 2: Payment unable to be made due to file-level errors"

                        )
                ),Arguments.of("TotalAmountIsMismatching", "REJECT",false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},2,7.00,AUD",
                                "P,2,,${account},7.69,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation",
                                "P,3,,${account},7.69,AUD,RTGS,Payment-number-2,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation 2"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: The header TotalAmount: 7.00 does not match the sum of the Detail amounts in the file: 15.38",
                                "E,Row 2: Payment unable to be made due to file-level errors",
                                "E,Row 3: Payment unable to be made due to file-level errors"
                        )
                ),Arguments.of("TotalAmountIsBlank --bug-- row number is coming wrong", "REJECT",false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,,AUD",
                                "P,2,,${account},7.69,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"
                        ), List.of(
                                "Record Type,Error Text",
                                "Row 2: missing mandatory field in column 4-Row 1: Payment header amount is invalid-Row 1: The header TotalAmount: 0 does not match the sum of the Detail amounts in the file: 7.69",
                                "E,Row 2: Payment unable to be made due to file-level errors"
                        )
                ),Arguments.of("TotalAmountIsString", "REJECT",false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,seven dollar,AUD",
                                "P,2,,${account},7.00,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 2: invalid field format 'seven dollar' in column 4-Row 1: Payment header amount is invalid-Row 1: The header TotalAmount: 0 does not match the sum of the Detail amounts in the file: 7.00",
                                "E,Row 2: Payment unable to be made due to file-level errors"
                        )
                ),Arguments.of("headerCurrencyIsBlank -- bug -- ,Invalid Request File provided when currnecy is blank", "REJECT",false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,",
                                "P,2,,${account},7.00,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"
                        ), List.of(
                                "Record Type,Error Text",
                                "",
                                "E,Row 2: Payment unable to be made due to file-level errors"
                        )
                ),Arguments.of("headerCurrencyIsInvalid", "REJECT",false, Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,aussi",
                                "P,2,,${account},7.00,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: Header currency is invalid",
                                "E,Row 2: Payment unable to be made due to file-level errors"
                        )
                ),Arguments.of("headerCurrencyIsNZD", "RESPONSE",false,
                        Map.of(3,List.of(3,4)),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,NZD",
                                "P,2,,${account},7.00,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"
                        ), List.of(
                                "Record Type,Total Count,Successful Count,Failed Count",
                                "H,1,0,1",
                                "P,2,QWNjb3VudDo4MGI1YWVjYzZkYWE5NzdlZTIyNGQyNGQ0ZDFkZTgwZTZlNTNlYjg1NTUwM2RkOTI2NmE0ZDI3MTBhZDM3OGU3,398335559,7.00,AUD,rtgs,RTGS Automation,,PAYMENT_FAILURE,currency code does not match header value,PAYMENT_FAILURE"
                        )
                ),Arguments.of("paymentRecordTypeIsIncorrect", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "Pay,2,,${account},7.00,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 3: Unknown record type provided"
                        )
                ),Arguments.of("paymentRecordTypeIsBlank", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                ",2,,${account},7.00,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 3: Unknown record type provided"
                        )
                ),Arguments.of("paymentRecordNumberIsBlank", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,,,${account},7.00,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 3: Invalid record number "
                        )
                ),Arguments.of("paymentRecordNumberIsNotInSequence", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,3,,${account},7.00,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 3: Invalid record number "
                        )
                ),Arguments.of("paymentRecordNumberIsNotInSequence", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,3,,${account},7.00,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 3: Invalid record number 3"
                        )
                ), Arguments.of("paymentRecordNumberIsString", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,two,,${account},7.00,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 3: Invalid record number two"
                        )
                ),Arguments.of("paymentRecordAccountIdDoesNotMatchWithAccountNumber", "RESPONSE",false,
                        Map.of(3,List.of(3,4)),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,accoundId,${account},7.00,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"
                        ), List.of(
                                "Record Type,Total Count,Successful Count,Failed Count",
                                "H,1,0,1",
                                "P,2,accoundId,000010112,7.00,AUD,rtgs,RTGS Automation,,PAYMENT_FAILURE,Account Number and Account Id incompatible,PAYMENT_FAILURE"
                        )
                ),
                //suppose to create reject but create repsonse file instead
                //Record Type,Total Count,Successful Count,Failed Count
                //H,1,0,1
                //P,2,not-provided,,7.00,AUD,rtgs,RTGS Automation,,PAYMENT_FAILURE,account Id or account number is required,PAYMENT_FAILURE
                Arguments.of("paymentRecordAccountDetailsAreMissing  -bug", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,,7.00,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"
                        ), List.of(
                                "Record Type,Total Count,Successful Count,Failed Count",
                                "H,1,0,1",
                                "P,2,accoundId,000010112,7.00,AUD,rtgs,RTGS Automation,,PAYMENT_FAILURE,Account Number and Account Id incompatible,PAYMENT_FAILURE"
                        )
                ),Arguments.of("paymentRecordAmountIsBlank -- bug -- rownumber messedup ", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,${account},,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"
                        ), List.of(
                                "Record Type,Total Count,Successful Count,Failed Count",
                                "E,Row 1: The header TotalAmount: 7.00 does not match the sum of the Detail amounts in the file: 0",
                                "E,Row 3: missing mandatory field in column 5"
                        )
                ),Arguments.of("paymentRecordAmountIsString --- rownumber messup and message incorrect", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,${account},Seven dollar,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: The header TotalAmount: 7.00 does not match the sum of the Detail amounts in the file: 0",
                                "E,Row 3: invalid field format 'Seven dollar' in column 5"
                        )
                ),Arguments.of("paymentRecordCurrencyIsBlank", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,${account},7.00,,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,All Payment details in the file are invalid-Fatal error in Payment detail record(s)",
                                "E,Row 3: missing mandatory field in column 6-currency is required"
                        )
                ),Arguments.of("paymentRecordCurrencyIsNotIsoFormat", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,${account},7.00,aussi Dollar,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,All Payment details in the file are invalid-Fatal error in Payment detail record(s)",
                                "E,Row 2: Payment detail currency is invalid"
                        )
                ),Arguments.of("paymentRecordCurrencyIsNotSameAsHeader", "RESPONSE",false,
                        Map.of(3,  List.of(3,4)),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,${account},7.00,NZD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"
                        ), List.of(
                                "Record Type,Total Count,Successful Count,Failed Count",
                                "H,1,0,1",
                                "P,2,QWNjb3VudDoyMDJjOTc5MDE2MTY1MzJmZmQ0OTA0MjdiM2QyNzMzNzFmN2NmN2M4NTQ3ZjNjYzNhNmQ1NDViYmVmMTQwNDdk,000010049,7.00,NZD,rtgs,RTGS Automation,,PAYMENT_FAILURE,currency code does not match header value,PAYMENT_FAILURE"
                        )
                ),Arguments.of("RTGS Payment", "RESPONSE",false,
                        Map.of(3,  List.of(3,4,9)),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,${account},7.00,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"
                        ), List.of(
                                "Record Type,Total Count,Successful Count,Failed Count",
                                "H,1,1,0",
                                "P,2,QWNjb3VudDo4MGI1YWVjYzZkYWE5NzdlZTIyNGQyNGQ0ZDFkZTgwZTZlNTNlYjg1NTUwM2RkOTI2NmE0ZDI3MTBhZDM3OGU3,398335559,7.00,AUD,rtgs,RTGS Automation,UGF5bWVudDo4ZmYyZTNhM2ZmOTc2MTFhZjQ3YjdiYmRhODcxMGQ3ZjIwNmQ2YTdhYzFkMzE5ZDFjZDk2MzdhYjY1MjcxMWYy,successful,,successful"
                        )
                ),Arguments.of("DE Payment", "RESPONSE",false,
                        Map.of(3,  List.of(3,4,9)),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,${account},7.00,AUD,DE,Payment-DE-1,266505,46157191,Payment-account-name-1,AUTO-PAY,DE Automation"
                        ), List.of(
                                "Record Type,Total Count,Successful Count,Failed Count",
                                "H,1,1,0",
                                "P,2,QWNjb3VudDo4MGI1YWVjYzZkYWE5NzdlZTIyNGQyNGQ0ZDFkZTgwZTZlNTNlYjg1NTUwM2RkOTI2NmE0ZDI3MTBhZDM3OGU3,398335559,7.00,AUD,de,DE Automation,UGF5bWVudDo4ZmYyZTNhM2ZmOTc2MTFhZjQ3YjdiYmRhODcxMGQ3ZjIwNmQ2YTdhYzFkMzE5ZDFjZDk2MzdhYjY1MjcxMWYy,successful,,successful"
                        )
                ),Arguments.of("NPP Payment", "RESPONSE",false,
                        Map.of(3,  List.of(3,4,9)),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,${account},7.00,AUD,NPP,Payment-npp-1,266505,46157191,Payment-account-name-1,AUTO-PAY,NPP Automation"
                        ), List.of(
                                "Record Type,Total Count,Successful Count,Failed Count",
                                "H,1,1,0",
                                "P,2,QWNjb3VudDo4MGI1YWVjYzZkYWE5NzdlZTIyNGQyNGQ0ZDFkZTgwZTZlNTNlYjg1NTUwM2RkOTI2NmE0ZDI3MTBhZDM3OGU3,398335559,7.00,AUD,npp,NPP Automation,UGF5bWVudDo4ZmYyZTNhM2ZmOTc2MTFhZjQ3YjdiYmRhODcxMGQ3ZjIwNmQ2YTdhYzFkMzE5ZDFjZDk2MzdhYjY1MjcxMWYy,successful,,successful"
                        )
                ),Arguments.of("PaymentTypeIsBlank", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,${account},7.00,AUD,,Payment-npp-1,266505,46157191,Payment-account-name-1,AUTO-PAY,NPP Automation"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,All Payment details in the file are invalid-Fatal error in Payment detail record(s)",
                                "E,Row 3: missing mandatory field in column 7"
                        )
                ),
                //expected reject but response file is created expected error == payment detail payment type is invalid
                Arguments.of("PaymentTypeIsTST --bug -- payment detail payment type is invalid", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,${account},7.00,AUD,TST,Payment-npp-1,266505,46157191,Payment-account-name-1,AUTO-PAY,NPP Automation"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,All Payment details in the file are invalid-Fatal error in Payment detail record(s)",
                                "E,Row 3: missing mandatory field in column 7"
                        )
                ),Arguments.of("PaymentInstructionReferenceIsBlank", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,${account},7.00,AUD,RTGS,,266505,46157191,Payment-account-name-1,AUTO-PAY,NPP Automation"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,All Payment details in the file are invalid-Fatal error in Payment detail record(s)",
                                "E,Row 3: missing mandatory field in column 8"
                        )
                ),Arguments.of("PaymentInstructionReferenceIsLongerThan16Chars", "RESPONSE",false,
                        Map.of(3,  List.of(3,4)),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,${account},7.00,AUD,RTGS,reference-000000019,266505,46157191,Payment-account-name-1,AUTO-PAY,NPP Automation"
                        ), List.of(
                                "Record Type,Total Count,Successful Count,Failed Count",
                                "H,1,0,1",
                                "P,2,QWNjb3VudDowNTFmMGQyYjM0NzAyYmY4NDE1YTE0OGYyNzgxMWFjMDM5ZGYyMTVlZjkzYzZhZTBkNjFkZWU5YjIzN2MzMjEw,217157293,7.00,AUD,rtgs,NPP Automation,,failed,Payment reference must be 1-18 characters,failed"
                        )
                ),Arguments.of("PaymentInstructionBsbIsBlank", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,${account},7.00,AUD,RTGS,reference-000000019,,46157191,Payment-account-name-1,AUTO-PAY,NPP Automation"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,All Payment details in the file are invalid-Fatal error in Payment detail record(s)",
                                "E,Row 3: missing mandatory field in column 9"
                        )
                ),Arguments.of("PaymentInstructionBsbIsMoreThan6Chars", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,${account},7.00,AUD,RTGS,reference-000000019,2665051,46157191,Payment-account-name-1,AUTO-PAY,NPP Automation"
                        ), List.of(
                                "Record Type,Total Count,Successful Count,Failed Count",
                                "H,1,0,1",
                                "P,2,QWNjb3VudDowNTFmMGQyYjM0NzAyYmY4NDE1YTE0OGYyNzgxMWFjMDM5ZGYyMTVlZjkzYzZhZTBkNjFkZWU5YjIzN2MzMjEw,217157293,7.00,AUD,rtgs,NPP Automation,,failed,Payment reference must be 1-18 characters,failed"
                        )
                ),Arguments.of("PaymentInstructionBsbIsLessThan6Chars", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,${account},7.00,AUD,RTGS,reference-000,26650,46157191,Payment-account-name-1,AUTO-PAY,NPP Automation"
                        ), List.of(
                                "",
                                "",
                                ""
                        )
                ),Arguments.of("PaymentInstructionAccountNumberIsBlank", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,${account},7.00,AUD,RTGS,reference-000,266504,,Payment-account-name-1,AUTO-PAY,NPP Automation"
                        ), List.of(
                                "",
                                "",
                                ""
                        )
                ),Arguments.of("PaymentInstructionAccountNumberTenChar", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,${account},7.00,AUD,RTGS,reference-000,266504,4615719119,Payment-account-name-1,AUTO-PAY,NPP Automation"
                        ), List.of(
                                "",
                                "",
                                ""
                        )
                ),Arguments.of("AccountNameIsBlank", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,${account},7.00,AUD,RTGS,reference-000,266504,461571911,,AUTO-PAY,NPP Automation"
                        ), List.of(
                                "",
                                "",
                                ""
                        )
                ),Arguments.of("AccountNameIsMoreThan35Chars", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,${account},7.00,AUD,RTGS,reference-000,266504,461571911,simulator simulator simulator simulator ,AUTO-PAY,NPP Automation"
                        ), List.of(
                                "",
                                "",
                                ""
                        )
                ),Arguments.of("Customer Transaction Reference and Narrative blank", "RESPONSE",false,
                        Map.of(3,List.of(3,4)),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,${account},7.00,AUD,RTGS,reference-000,266504,461571911,accountName,,"
                        ), List.of(
                                "Record Type,Total Count,Successful Count,Failed Count",
                                "H,1,1,0",
                                "P,2,QWNjb3VudDozMDExYmYwMzFkN2EyMGU0ZGRiMzQzYTFjY2NmMjFlOTE5ZWFkMDZhZDMzZWI4NTM1MDE5M2RhNDE5NGI0YjEy,000010013,7.00,AUD,rtgs,,UGF5bWVudDpiYWVmODljZmE2NTk4MTgwN2M5MmQ1OGVmM2UwMDQzMThlNDg1ZjM3ODdiZmEzOTYyZDQ2MGE1YTY3ZjM5Yjc2,successful,,successful"

                        )
                ),Arguments.of("Customer Transaction Reference and Narrative long", "RESPONSE",false,
                        Map.of(3,List.of(3,4)),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,${account},7.00,AUD,RTGS,reference-000,266504,461571911,accountName,AUTO-PAY AUTO-PAYAUTO-PAYAUTO-PAYAUTO-PAYAUTO-PAYAUTO-PAYAUTO-PAY,NPP AutomationNPP AutomationNPP AutomationNPP AutomationNPP AutomationNPP AutomationNPP Automation"
                        ), List.of(
                                "Record Type,Total Count,Successful Count,Failed Count",
                                "H,1,1,0",
                                "P,2,QWNjb3VudDozMDExYmYwMzFkN2EyMGU0ZGRiMzQzYTFjY2NmMjFlOTE5ZWFkMDZhZDMzZWI4NTM1MDE5M2RhNDE5NGI0YjEy,000010013,7.00,AUD,rtgs,NPP AutomationNPP AutomationNPP AutomationNPP AutomationNPP AutomationNPP AutomationNPP Automation,UGF5bWVudDo2ZGQ3ZjUzMTdhYjNiYmVjMmQxOWIzYjM3ZmRlMDczOGU0YmVjMmVmOGIxYWI4M2UwYTdkODdlOTVkOWM4OTBj,successful,,successful\n"

                        )
                ),Arguments.of("PaymentRecordMultipleErrors", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.00,AUD",
                                "P,2,,,,,,,,,,,"
                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: The header TotalAmount: 7.00 does not match the sum of the Detail amounts in the file: 0",
                                "E,Row 3: missing mandatory field in column 5-Row 3: missing mandatory field in column 6-Row 3: missing mandatory field in column 7-Row 3: missing mandatory field in column 8-Row 3: missing mandatory field in column 9-Row 3: missing mandatory field in column 10-Row 3: missing mandatory field in column 11"
                        )
                ),Arguments.of("new", "REJECT",false,
                        Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,7.00,AUD",
                                "3,P,,${account},7.00,AUD,DE,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,Automation,2026-06-18,sydney,AU"
                        ), List.of(
                                "Record Type,Total Count,Successful Count,Failed Count",
                                "H,1,0,1",
                                "P,2,QWNjb3VudDoyMDJjOTc5MDE2MTY1MzJmZmQ0OTA0MjdiM2QyNzMzNzFmN2NmN2M4NTQ3ZjNjYzNhNmQ1NDViYmVmMTQwNDdk,000010049,7.00,NZD,rtgs,RTGS Automation,,PAYMENT_FAILURE,currency code does not match header value,PAYMENT_FAILURE"
                        )
                )
                
                
                
        );
    }

    static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of("new", "RESPONSE",false,
                        Collections.emptyMap(),
                        List.of(
                                "1,V,2.0",
                                "2,H,${authlink},1,7.10,AUD",
                                "3,P,,${account},7.10,AUD,RTGS,${reference},266505,46157191,Payment-account-name-1,AUTO-PAY,Automation,2026-06-22,,"
                        ), List.of(
                                "Record Type,Total Count,Successful Count,Failed Count"
                        )
                )
        );
    }


}
