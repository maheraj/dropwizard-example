package com.example.bookingwallet.core;

import com.example.bookingwallet.core.constant.Operation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private long id;
    private Date date;
    private String originalCurrencyCode;
    private double originalAmount;
    private Set<TransactionPart> transactionParts;
    private Long parentId;
    private Operation operation;
    private String notes;


    public void addTransactionPart(TransactionPart part) {
        if (this.transactionParts == null) {
            this.transactionParts = new HashSet<>();
        }
        this.transactionParts.add(part);
    }

}
