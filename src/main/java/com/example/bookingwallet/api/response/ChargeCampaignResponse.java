package com.example.bookingwallet.api.response;

import com.example.bookingwallet.core.Transaction;
import com.example.bookingwallet.core.TransactionPart;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ChargeCampaignResponse {
    private long transactionId;
    private long campaignId;
    private long campaignWalletId;
    private long customerWalletId;
    private String currencyCode;
    private double transactionAmount;
    private List<Link> links = new ArrayList<>();

    public ChargeCampaignResponse(long campaignId, Transaction transaction) {
        this.transactionId = transaction.getId();
        this.campaignId = campaignId;
        this.currencyCode = transaction.getOriginalCurrencyCode();
        this.transactionAmount = transaction.getOriginalAmount();

        for (TransactionPart part : transaction.getTransactionParts()) {
            if (part.getDirection().isDebit()) {
                this.campaignWalletId = part.getWalletId();
            } else {
                this.customerWalletId = part.getWalletId();
            }
        }
    }
}
