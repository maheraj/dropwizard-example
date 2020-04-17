package com.example.bookingwallet.util;

import java.math.BigDecimal;

public enum EuroCurrencyRate {
    EUR(new BigDecimal("1.00")),
    USD(new BigDecimal("0.92396")),
    JPY(new BigDecimal("0.00857")),
    GBP(new BigDecimal("1.14817")),
    CHF(new BigDecimal("0.95090")),
    AUD(new BigDecimal("0.58453")),
    CAD(new BigDecimal("0.65488"));
    BigDecimal rate;

    EuroCurrencyRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRate() {
        return this.rate;
    }
}
