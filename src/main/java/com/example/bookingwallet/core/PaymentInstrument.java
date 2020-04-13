package com.example.bookingwallet.core;

import com.example.bookingwallet.core.constant.InstrumentType;

public class PaymentInstrument {
    private long id;
    private long walletId;
    private InstrumentType type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWalletId() {
        return walletId;
    }

    public void setWalletId(long walletId) {
        this.walletId = walletId;
    }

    public InstrumentType getType() {
        return type;
    }

    public void setType(InstrumentType type) {
        this.type = type;
    }
}
