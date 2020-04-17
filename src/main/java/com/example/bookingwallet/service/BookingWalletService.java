package com.example.bookingwallet.service;

import com.example.bookingwallet.core.Campaign;
import com.example.bookingwallet.core.Transaction;
import com.example.bookingwallet.core.TransactionPart;
import com.example.bookingwallet.core.Wallet;
import com.example.bookingwallet.db.jdbi.CampaignDao;
import com.example.bookingwallet.db.jdbi.TransactionDao;
import com.example.bookingwallet.db.jdbi.TransactionPartDao;
import com.example.bookingwallet.db.jdbi.WalletDao;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class BookingWalletService {
    public static final long EXPENSE_WALLET_ID = 1;
    private CampaignDao campaignDao;
    private WalletDao walletDao;
    private TransactionDao transactionDao;
    private TransactionPartDao transactionPartDao;

    public BookingWalletService(Jdbi jdbi) {
        campaignDao = jdbi.onDemand(CampaignDao.class);
        walletDao = jdbi.onDemand(WalletDao.class);
        transactionDao = jdbi.onDemand(TransactionDao.class);
        transactionPartDao = jdbi.onDemand(TransactionPartDao.class);
    }

    public Campaign getCampaignById(long campaignId) {
        return campaignDao.getById(campaignId);
    }

    public Wallet getWalletById(long walletId) {
        return walletDao.getById(walletId);
    }

    public void createWallet(Wallet wallet) {
        wallet.setId(walletDao.insert(wallet));
    }

    public void createCampaign(Campaign campaign) {
        campaign.setId(campaignDao.insert(campaign));
    }

    public double getWalletBalance(long walletId) {
        return transactionPartDao.getWalletBalance(walletId);
    }

    public void createTransaction(Transaction transaction) {
        transaction.setId(transactionDao.insert(transaction));
    }

    public void createTransactionPart(TransactionPart transactionPart) {
        transactionPart.setId(transactionPartDao.insert(transactionPart));
    }

    public List<Wallet> listWallets() {
        return walletDao.listWallets();
    }

    public Wallet getExpenseWallet() {
        return walletDao.getById(EXPENSE_WALLET_ID);
    }
}
