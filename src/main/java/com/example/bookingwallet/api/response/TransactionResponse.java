package com.example.bookingwallet.api.response;

import com.example.bookingwallet.core.Transaction;
import com.example.bookingwallet.core.TransactionPart;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ApiModel("TransactionResponse")
public class TransactionResponse {
    private long transactionId;
    private long debitWalletId;
    private long creditWalletId;
    private String currency;
    private double transactionAmount;
    private double refundedAmount;
    private List<Link> links = new ArrayList<>();

    public TransactionResponse(Transaction transaction) {
        this.transactionId = transaction.getId();
        for (TransactionPart part : transaction.getTransactionParts()) {
            if (part.getDirection().isDebit()) {
                if (!part.isRefund()) {
                    this.debitWalletId = part.getWalletId();
                    this.currency = part.getCurrency();
                    this.transactionAmount += part.getAmount();
                }
            } else {
                if (!part.isRefund()) {
                    this.creditWalletId = part.getWalletId();
                } else {
                    this.refundedAmount += part.getAmount();
                }
            }
        }
    }
}
