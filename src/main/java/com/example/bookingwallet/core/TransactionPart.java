package com.example.bookingwallet.core;

import com.example.bookingwallet.core.constant.TransactionDirection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionPart implements Cloneable {
    private long id;
    private long transactionId;
    private long walletId;
    private TransactionDirection direction;
    private double amount;
    private String currencyCode;
    //private boolean refund;// remove this

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
