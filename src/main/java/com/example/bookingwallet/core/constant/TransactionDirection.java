package com.example.bookingwallet.core.constant;

public enum TransactionDirection {
    DEBIT,
    CREDIT;

    public boolean isDebit() {
        return this == DEBIT;
    }
}
