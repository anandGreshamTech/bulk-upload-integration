package com.gresham.bulk.upload.dataprovider;


import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class AggregatePaymentDataProvider {
     static Stream<Arguments> dataFull() {
        return Stream.of(
                Arguments.of("happyPath", "RESPONSE","claName",
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,Automation-2.92,Automation,2.92,AUD,DEBIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,DEBIT,narrative,tracingId for 2.9156976"
    
                        ), List.of(
                                "Record Type,Error Text",
                                "E,\"Row 1: [The AuthLink in the file name does not match the Customer Code in the header, Unable to retrieve Customer record for AuthLink in file name]\"", 
                                "E,Row 2: null"
                        )
                ),Arguments.of("invalidHeaderRecordType", "REJECT","claName",
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "A,${authlink},1,Automation-2.92,Automation,2.92,AUD,DEBIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,DEBIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [header recordType is invalid]"

                        )
                ),Arguments.of("emptyHeaderRecordType", "REJECT","claName",
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                ",${authlink},1,Automation-2.92,Automation,2.92,AUD,DEBIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,DEBIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,\"Row 1: [recordType is required, header recordType is invalid]\""

                        )
                ),
                Arguments.of("emptyCustomerName", "REJECT","claName",
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,,1,Automation-2.92,Automation,2.92,AUD,DEBIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,DEBIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,\"Row 1: [customerCode is required, The AuthLink in the file name does not match the Customer Code in the header]\""

                        )
                ),
                Arguments.of("customerNameMismatch", "REJECT","claName",
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,TTT,1,Automation-2.92,Automation,2.92,AUD,DEBIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,DEBIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [The AuthLink in the file name does not match the Customer Code in the header]"

                        )
                ),
                Arguments.of("countMismatch", "REJECT","claName",
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},2,Automation-2.92,Automation,2.92,AUD,DEBIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,DEBIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [The header Instruction Count does not match the number of Instructions in the file]"

                        )
                ),
                Arguments.of("countBlack", "REJECT","claName",
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},,Automation-2.92,Automation,2.92,AUD,DEBIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,DEBIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,\"Row 1: [recordCount is required, Invalid value provided for Instruction Count]\""

                        )
                ),
                Arguments.of("countIsChar", "REJECT","claName",
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},one,Automation-2.92,Automation,2.92,AUD,DEBIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,DEBIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [Invalid value provided for Instruction Count]"

                        )
                ),
                Arguments.of("countNegative", "REJECT","claName",
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},-1,Automation-2.92,Automation,2.92,AUD,DEBIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,DEBIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [Invalid value provided for Instruction Count]"

                        )
                ),
                Arguments.of("referenceIsBlank", "REJECT","claName",
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,,Automation,2.92,AUD,DEBIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,DEBIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [reference is required]"

                        )
                ),Arguments.of("totalIsBlank", "REJECT","claName",
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,Automation-2.92,Automation,,AUD,DEBIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,DEBIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,\"Row 1: [totalAmount is required, totalAmount is invalid][The header TotalAmount does not match the sum of the Instruction amounts in the file]\"]"

                        )
                ),Arguments.of("totalMismatch", "REJECT","claName",
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,Automation-2.92,Automation,2.90,AUD,DEBIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,DEBIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [The header TotalAmount does not match the sum of the Instruction amounts in the file]"

                        )
                ),Arguments.of("headerCurrencyBlank", "REJECT","claName",
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,Automation-2.92,Automation,2.92,,DEBIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,DEBIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [currency is required]"

                        )
                )
                );
    }
     static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of("headerCurrencyMismatch", "REJECT","claName",
                        List.of(
                                "Record Type,Customer Name,Count,Reference,Customer Reference,Total Amount,Currency,Type,CLA ID,CLA Name,External Account Number,External Bank Code,External Name,External Payment Type",
                                "H,${authlink},1,Automation-2.92,Automation,2.92,australian doller,DEBIT,,${claName},,,,",
                                "I,2,,${account},2.92,AUD,DEBIT,narrative,tracingId for 2.9156976"

                        ), List.of(
                                "Record Type,Error Text",
                                "E,Row 1: [The header TotalAmount does not match the sum of the Instruction amounts in the file]"

                        )
                ));
    }


}
