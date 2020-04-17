package com.example.bookingwallet.api.response;

import com.example.bookingwallet.core.Wallet;
import com.example.bookingwallet.core.constant.WalletType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class WalletBalanceResponse {
    private long walletId;
    private Long customerId;
    private WalletType walletType;
    private double balance;
    private String currencyCode;

    private List<Link> links = new ArrayList<>();

    public WalletBalanceResponse(Wallet wallet, double balance) {
        this.walletId = wallet.getId();
        this.customerId = wallet.getCustomerId();
        this.walletType = wallet.getWalletType();
        this.balance = balance;
        this.currencyCode = wallet.getCurrencyCode();
        this.links.add(new Link("self", "/wallets?customerId=" + this.customerId));
    }
}
