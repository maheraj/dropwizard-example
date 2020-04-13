package com.example.bookingwallet.core;

import com.example.bookingwallet.core.constant.WalletType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Wallet {
    private long id;
    private String currency;
    private WalletType walletType;
    private Long customerId;

    public Wallet(WalletType walletType, String currency) {
        this.walletType = walletType;
        this.currency = currency;
    }

    public Wallet(long id, WalletType walletType, String currency) {
        this(walletType, currency);
        this.id = id;
    }
}
