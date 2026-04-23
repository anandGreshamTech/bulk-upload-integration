package com.gresham.bulk.upload.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRecord {
    @Builder.Default
    private String recordType="T";
    private String recordNumber;
    private String debitingAccountNumber;
    private String creditingAccountNumber;
    private String amount;
    private String currency;
    private String customerTransactionReference;
    private String payerReference;
    private String payeeReference;
    @Override
    public String toString() {
        return recordNumber+","+recordType+","+ debitingAccountNumber +","+ creditingAccountNumber
                +","+amount+","+currency+","+customerTransactionReference+","+ payerReference +","+ payeeReference
                +"\n";
    }
}
