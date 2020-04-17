package com.example.bookingwallet.api.response;

import com.example.bookingwallet.core.Transaction;
import com.example.bookingwallet.core.TransactionPart;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ChargeCustomerResponse {
    private long transactionId;
    private long customerWalletId;
    private long expenseWalletId;
    private String currency;
    private double transactionAmount;
    private double refundedAmount;
    private List<Link> links = new ArrayList<>();

    public ChargeCustomerResponse(Transaction transaction) {
        this.transactionId = transaction.getId();
        for (TransactionPart part : transaction.getTransactionParts()) {
            if (part.getDirection().isDebit()) {
                    this.customerWalletId = part.getWalletId();
                    this.currency = part.getCurrencyCode();
                    this.transactionAmount += part.getAmount();
            } else {
                    this.expenseWalletId = part.getWalletId();
            }
        }
    }
}
