package com.example.bookingwallet.core;

import com.example.bookingwallet.core.constant.TransactionDirection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionPart implements Cloneable {
    private long id;
    private TransactionDirection direction;
    private long walletId;
    private double amount;
    private String currency;
    private long instrumentId;
    private long transactionId;
    private boolean refund;

    // Overriding clone() method of Object class
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
