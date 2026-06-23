package com.gresham.bulk.upload.dataprovider;


import org.junit.jupiter.params.provider.Arguments;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CloseAccountDataProviderV2 {
    static Stream<Arguments> fullSet() {
        return Stream.of(
                Arguments.of("fullSetOfValidData", "ACCEPT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,1.0",
                                "2,H,POMPOM,1",
                                "3,A,,${account},${bsb},${recipientAccount},${recipientName}"

                        ),List.of(
                                "Record Type,Error Text",
                                "E,Row 3: Unknown record type provided"
                        )
                )
        );
    }
    static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of("headerRecordNumberIsBlank", "REJECT", false, Collections.emptyMap(),
                        List.of(
                                "1,V,1.0",
                                ",H,${authlink},1",
                                "3,C,,${account},${bsb},${recipientAccount},${recipientName}"

                        ),List.of(
                                "Record Type,Error Text",
                                "E,Row 3: Unknown record type provided"
                        )
                )
        );
    }



}
