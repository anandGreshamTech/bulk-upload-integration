package com.gresham.bulk.upload.compare;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;



public class DataCompareService {
    
    public void assertFileData(
            List<List<String>> expected,
            List<List<String>> actual,
            boolean ignoreHeader,
            Map<Integer, List<Integer>> columnsToIgnoreByRow
    ) {
        Assertions.assertEquals(expected.size(), actual.size(), "Row count mismatch");

        int startRow = ignoreHeader ? 1 : 0;

        for (int row = startRow; row < expected.size(); row++) {

            List<String> expectedRow = expected.get(row);
            List<String> actualRow = actual.get(row);

            Assertions.assertEquals(
                    expectedRow.size(),
                    actualRow.size(),
                    "Column count mismatch at row " + (row + 1)
            );

            List<Integer> columnsToIgnore =
                    columnsToIgnoreByRow.getOrDefault(row + 1, Collections.emptyList());

            for (int col = 0; col < expectedRow.size(); col++) {

                int columnNumber = col + 1;

                if (columnsToIgnore.contains(columnNumber)) {
                    continue;
                }

                Assertions.assertEquals(
                        expectedRow.get(col),
                        actualRow.get(col),
                        "Mismatch at row " + (row + 1)
                        + ", column " + columnNumber
                );
            }
        }
    }
}
