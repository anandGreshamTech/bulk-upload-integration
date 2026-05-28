package com.gresham.bulk.upload.dataprovider;


import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class PaymentDataProvider {
    static Stream<Arguments> dataOriginal() {
        return Stream.of(
                Arguments.of("invalidHeaderIdentifier", "REJECT",
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "He,${authlink},1,7.69,INR",
                                "P,2,,${account},7.69,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [header recordType is invalid]"
                        )
                ),Arguments.of("currencyRequired", "REJECT",
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
                Arguments.of("currencyInvalid", "REJECT",
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.69,aussiDoller",
                                "P,2,,${account},7.69,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [header currency is invalid]",
                                "E,Row 2: null"
                        )
                ),Arguments.of("currencyMismatchWithPaymentRecord", "RESPONSE",
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
                Arguments.of("customerCodeRequired", "REJECT",
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,,1,7.69,AUD",
                                "P,2,,${account},7.69,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [customerCode is required][The AuthLink in the file name does not match the Customer Code in the header]",
                                "E,Row 2: File validation failed"
                        )
                ),Arguments.of("HeaderIndentifierBlank", "REJECT",
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                ",${authlink},1,7.69,AUD",
                                "P,2,,${account},7.69,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,\"Row 1: [recordType is required, header recordType is invalid]\"",
                                "E,Row 2: File validation failed"
                        )
                ),Arguments.of("incorrectAuthlinkInHeader", "REJECT",
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,Test,1,7.69,AUD",
                                "P,2,,${account},7.69,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [The AuthLink in the file name does not match the Customer Code in the header]",
                                "E,Row 2: File validation failed"
                        )
                )
                
        );
    }

    static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of("incorrectAuthlinkInFileName", "REJECT",
                        List.of(
                                "Record Type,Customer Code,Total Count,Total Amount,Currency",
                                "H,${authlink},1,7.69,AUD",
                                "P,2,,${account},7.69,AUD,RTGS,Payment-number-1,266505,46157191,Payment-account-name-1,AUTO-PAY,RTGS Automation"
    
                        ), List.of(
                                "Record Type,Error Text",
                                "E,\"Row 1: [The AuthLink in the file name does not match the Customer Code in the header, Unable to retrieve Customer record for AuthLink in file name]\"", 
                                "E,Row 2: null"
                        )
                ));
    }


}
