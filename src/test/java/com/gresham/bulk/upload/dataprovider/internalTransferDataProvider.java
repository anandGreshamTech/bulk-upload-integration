package com.gresham.bulk.upload.dataprovider;


import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class internalTransferDataProvider {
    static Stream<Arguments> HeaderInvalidScenarios() {
        return Stream.of(
                Arguments.of("CurrencyBlank","REJECT","2,H,%s,1,14.42,",List.of(
                        "Record Type,Error Text",
                        "E,Row 2: missing mandatory field in column 6-currency is required",
                        "E,Row 3: Internal Transfer unable to be made due to file-level errors"
                )),Arguments.of("totalAmountNotMatching","REJECT","2,H,%s,1,14.42,AUD",List.of(
                        "Record Type,Error Text",
                        "E,Row 2: "
                )),Arguments.of("InvalidRecordCount","REJECT","2,H,%s,20,14.41,AUD",List.of(
                        "Record Type,Error Text",
                        "E,Row 2: Number of Transfers defined in Header: 20 does not match number of Transfer details in file: 1"
                )),Arguments.of("InvalidCustomerCode","REJECT","2,X,TEST,1,14.41,AUD",List.of(
                        "Record Type,Error Text",
                        "E,Row 2: Unknown record type provided"
                )));
    }


}
