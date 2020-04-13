package com.example.bookingwallet.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Campaign {
    private long id;
    private String name;
    private String currency;
    private double balance;
    private double budget;
    private Wallet wallet;
    private Long lastTransactionId;

    public Campaign(String name, String currency, double budget) {
        this.name = name;
        this.currency = currency;
        this.budget = budget;
    }
}
