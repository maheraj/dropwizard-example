package com.example.bookingwallet.api.response;

import com.example.bookingwallet.core.Wallet;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ApiModel("Wallet")
public class WalletResponse {
    private long walletId;
    private long customerId;
    private String currency;
    private double balance;
    private List<Link> links = new ArrayList<>();

    public WalletResponse(Wallet wallet) {
        this.walletId = wallet.getId();
        this.customerId = wallet.getCustomerId();
        this.currency = wallet.getCurrency();
        this.links.add(new Link("self", "/wallets?customerId=" + this.customerId));
    }
}
