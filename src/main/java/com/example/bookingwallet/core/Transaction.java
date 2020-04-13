package com.example.bookingwallet.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    private long id;
    private Date date;
    private boolean refunded;
    private Set<TransactionPart> transactionParts = new HashSet<>();
}
