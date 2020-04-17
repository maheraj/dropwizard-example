package com.example.bookingwallet.api.response;

import com.example.bookingwallet.core.Transaction;
import com.example.bookingwallet.core.TransactionPart;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RefundCustomerResponse {
    private long transactionId;
    private long customerWalletId;
    private long expenseWalletId;
    private String currencyCode;
    private double refundedAmount;
    private List<Link> links = new ArrayList<>();

    public RefundCustomerResponse(Transaction transaction) {
        /*
        this.transactionId = transaction.getId();
        for (TransactionPart part : transaction.getTransactionParts()) {
            if (part.isRefund()) {
                if (part.getDirection().isCredit()) {
                    this.customerWalletId = part.getWalletId();
                    this.refundedAmount += part.getAmount();
                    this.currencyCode = part.getCurrencyCode();
                } else {
                    this.expenseWalletId = part.getWalletId();
                }
            }
        }

         */
    }
}
