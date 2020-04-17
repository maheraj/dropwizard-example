package com.example.bookingwallet.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {
    private long id;
    private String name;
    private double balance;
    private double budget;
    private long walletId;
    private Long lastTransactionId;

    private Wallet wallet;

    public Campaign(String name, double budget) {
        this.name = name;
        this.budget = budget;
    }
}
