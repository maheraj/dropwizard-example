package com.example.bookingwallet.api.response;

import com.example.bookingwallet.core.Transaction;
import com.example.bookingwallet.core.TransactionPart;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RefundCampaignResponse {
    private long transactionId;
    private long campaignId;
    private long campaignWalletId;
    private long customerWalletId;
    private String currencyCode;
    private double refundedAmount;
    private List<Link> links = new ArrayList<>();

    public RefundCampaignResponse(long campaignId, Transaction transaction) {
        /*
        this.transactionId = transaction.getId();
        this.campaignId = campaignId;
        for (TransactionPart part : transaction.getTransactionParts()) {
            if (part.isRefund()) {
                if (part.getDirection().isCredit()) {
                    this.campaignWalletId = part.getWalletId();
                    this.refundedAmount += part.getAmount();
                    this.currencyCode = part.getCurrencyCode();
                } else {
                    this.customerWalletId = part.getWalletId();
                }
            }
        }

         */
    }
}
