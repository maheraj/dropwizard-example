package com.example.bookingwallet.db;

import com.example.bookingwallet.core.Wallet;
import com.example.bookingwallet.core.constant.WalletType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WalletDB {
    private static Map<Long, Wallet> wallets = new HashMap<>();
    public static long SEQUENCE = 0;

    static {
        wallets.put(1l, new Wallet(nextSequence(), WalletType.EXPENSE_WALLET, "USD"));
    }

    public static long nextSequence() {
        return ++SEQUENCE;
    }

    public static Wallet getById(long id) {
        return wallets.get(id);
    }

    public static List<Wallet> getAll() {
        List<Wallet> result = new ArrayList<Wallet>();
        for (Long key : wallets.keySet()) {
            result.add(wallets.get(key));
        }
        return result;
    }

    public static void createWallet(Wallet wallet) {
        wallet.setId(nextSequence());
        wallets.put(wallet.getId(), wallet);
    }

    public static void removeWallet(long id) {
        if (!wallets.keySet().isEmpty()) {
            wallets.remove(id);
        }
    }

    public static int getCount() {
        return wallets.size();
    }


    public static Wallet getByCustomerId(long customerId) {
        for (Wallet wallet : wallets.values()) {
            if (wallet.getCustomerId() == customerId) {
                return wallet;
            }
        }
        return null;
    }
}
