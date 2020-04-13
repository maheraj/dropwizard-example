package com.example.bookingwallet.db;

import com.example.bookingwallet.core.Transaction;
import com.example.bookingwallet.core.TransactionPart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionDB {
    private static Map<Long, Transaction> transactions = new HashMap<>();
    private static Map<Long, TransactionPart> transactionParts = new HashMap<>();
    public static long SEQUENCE = 0;

    public static long nextSequence() {
        return ++SEQUENCE;
    }

    public static Transaction getById(long id) {
        return transactions.get(id);
    }

    public static List<Transaction> getAll() {
        List<Transaction> result = new ArrayList<Transaction>();
        for (Long key : transactions.keySet()) {
            result.add(transactions.get(key));
        }
        return result;
    }

    public static void createTransaction(Transaction transaction) {
        transaction.setId(nextSequence());
        transactions.put(transaction.getId(), transaction);

        for (TransactionPart transactionPart : transaction.getTransactionParts()) {
            transactionPart.setTransactionId(transaction.getId());
            createTransactionParts(transactionPart);
        }
    }

    public static void createTransactionParts(TransactionPart transactionPart) {
        transactionPart.setId(nextSequence());
        transactionParts.put(transactionPart.getId(), transactionPart);
    }

}
