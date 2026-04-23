package com.gresham.bulk.upload.dataprovider;


import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class CloseAccountDataProvider {
    static Stream<Arguments> headerInvalidScenarios() {
        return Stream.of(
                Arguments.of("invalidClosureType","REJECT",
                        List.of(
                                "1,V,1.0",
                                "2,H,POMPOM,1",
                                "3,A,,${account},${bsb},${recipientAccount},${recipientName}"

                        ),List.of(
                        "Record Type,Error Text", 
                                "E,Row 3: Unknown record type provided"
                ))/*
                Arguments.of("invalidClosureRowId","REJECT",
                        List.of(
                                "1,V,1.0",
                                "2,H,POMPOM,1",
                                "99,C,,${account},${bsb},${recipientAccount},${recipientName}"

                        ),List.of(
                        "Record Type,Error Text", 
                                "E,Row 3: Invalid record number 99"
                )),Arguments.of("invalidCustomerCode","REJECT",
                        List.of(
                                "1,V,1.0",
                                "2,H,POMPOM,1",
                                "3,C,,${account},${bsb},${recipientAccount},${recipientName}"

                        ),List.of(
                        "Record Type,Error Text", 
                                "E,Row 2: The Authlink in the file name does not match the Customer Code in the header",
                                "E,Row 3: Account unable to be closed due to file-level errors"
                )),Arguments.of("invalidHeaderRowId","REJECT",
                        List.of(
                                "1,V,1.0",
                                "4,A,${authlink},1",
                                "3,C,,${account},${bsb},${recipientAccount},${recipientName}"

                        ),List.of(
                        "Record Type,Error Text","E,Row 2: Invalid record number 4"
                )),Arguments.of("unknownRecordTypeProvided","REJECT",
                        List.of(
                                "1,V,1.0",
                                "2,A,${authlink},2",
                                "3,C,,${account},${bsb},${recipientAccount},${recipientName}"

                        ),List.of(
                        "Record Type,Error Text","E,Row 2: Unknown record type provided"
                )),Arguments.of("invalidTotalCount","REJECT",
                        List.of(
                                "1,V,1.0",
                                "2,H,${authlink},2",
                                "3,C,,${account},${bsb},${recipientAccount},${recipientName}"

                        ),List.of(
                        "Record Type,Error Text","E,Row 2: Number of Accounts defined in Header: 2 does not match number of Accounts in file: 1"
                )),Arguments.of("invalidHeaderValue","REJECT",
                        List.of(
                                "1,V,1.0",
                                "2,A,${authlink},1",
                                "3,C,,${account},183043,365763672,Sherwood Reichert"

                        ),List.of(
                        "Record Type,Error Text"
                ))*/);
    }


}
