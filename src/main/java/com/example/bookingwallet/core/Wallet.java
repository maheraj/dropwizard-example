package com.example.bookingwallet.core;

import com.example.bookingwallet.core.constant.WalletType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
    private long id;
    private String currencyCode;
    private WalletType walletType;
    private Long customerId;

    public Wallet(WalletType walletType, String currencyCode) {
        this.walletType = walletType;
        this.currencyCode = currencyCode;
    }

    public Wallet(long id, WalletType walletType, String currencyCode) {
        this(walletType, currencyCode);
        this.id = id;
    }
}
