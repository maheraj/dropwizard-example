package com.example.bookingwallet.api.response;

import com.example.bookingwallet.core.Wallet;
import com.example.bookingwallet.core.constant.WalletType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class WalletResponse {
    private long walletId;
    private Long customerId;
    private String currencyCode;
    private WalletType walletType;
    private List<Link> links = new ArrayList<>();

    public WalletResponse(Wallet wallet) {
        this.walletId = wallet.getId();
        this.walletType = wallet.getWalletType();
        this.customerId = wallet.getCustomerId();
        this.currencyCode = wallet.getCurrencyCode();
        this.links.add(new Link("self", "/wallets?customerId=" + this.customerId));
    }
}
