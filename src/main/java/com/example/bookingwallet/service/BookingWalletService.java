package com.example.bookingwallet.service;

import com.example.bookingwallet.api.request.ChargeCampaignRequest;
import com.example.bookingwallet.api.request.CreateCampaignRequest;
import com.example.bookingwallet.core.Campaign;
import com.example.bookingwallet.core.Transaction;
import com.example.bookingwallet.core.TransactionPart;
import com.example.bookingwallet.core.Wallet;
import com.example.bookingwallet.core.constant.Operation;
import com.example.bookingwallet.core.constant.TransactionDirection;
import com.example.bookingwallet.core.constant.WalletType;
import com.example.bookingwallet.db.jdbi.CampaignDao;
import com.example.bookingwallet.db.jdbi.TransactionDao;
import com.example.bookingwallet.db.jdbi.TransactionPartDao;
import com.example.bookingwallet.db.jdbi.WalletDao;
import com.example.bookingwallet.util.CurrencyExchangeUtil;
import org.jdbi.v3.core.Handle;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.util.Date;
import java.util.List;

public class BookingWalletService {
    public static final long EXPENSE_WALLET_ID = 1;
    private CampaignDao campaignDao;
    private WalletDao walletDao;
    private TransactionDao transactionDao;
    private TransactionPartDao transactionPartDao;
    private Handle handle;

    public BookingWalletService(Handle handle) {
        this.handle = handle;
        campaignDao = handle.attach(CampaignDao.class);
        walletDao = handle.attach(WalletDao.class);
        transactionDao = handle.attach(TransactionDao.class);
        transactionPartDao = handle.attach(TransactionPartDao.class);
    }

    public Campaign getCampaignById(long campaignId) {
        return campaignDao.getById(campaignId);
    }

    public Wallet getWalletById(long walletId) {
        return walletDao.getById(walletId);
    }

    private Transaction getTransactionById(long transactionId) {
        return transactionDao.getById(transactionId);
    }

    private List<TransactionPart> getTransactionPartsByTransactionId(long transactionId) {
        return transactionPartDao.getByTransactionId(transactionId);
    }

    //insert 1
    public void createWallet(Wallet wallet) {
        this.handle.useTransaction(handle1 -> wallet.setId(walletDao.insert(wallet)));
    }

    //insert 2
    private void createCampaign(Campaign campaign) {
        this.handle.useTransaction(handle1 -> campaign.setId(campaignDao.insert(campaign)));

    }

    public double getWalletBalance(long walletId) {
        return transactionPartDao.getWalletBalance(walletId);
    }

    //insert 3
    public void createTransaction(Transaction transaction) {
        this.handle.useTransaction(handle1 -> transaction.setId(transactionDao.insert(transaction)));
    }

    //insert 4
    public void createTransactionPart(TransactionPart transactionPart) {
        this.handle.useTransaction(handle1 -> transactionPart.setId(transactionPartDao.insert(transactionPart)));
    }

    public List<Wallet> listWallets() {
        return walletDao.listWallets();
    }

    public Wallet getExpenseWallet() {
        return walletDao.getById(EXPENSE_WALLET_ID);
    }


    //method A with insert 1,2
    public Campaign saveCampaign(CreateCampaignRequest request) {
        return this.handle.inTransaction(handle1 -> {
            //create campaign wallet
            Wallet wallet = new Wallet(0, request.getCurrencyCode(), WalletType.CAMPAIGN_WALLET, null);

            this.createWallet(wallet);
            //create campaign
            Campaign campaign = new Campaign(0, request.getName(), 0, request.getBudget(), wallet.getId(), null, wallet);
            this.createCampaign(campaign);

            return campaign;
        });
    }

    //method A with insert 3,4
    public Transaction saveChargeCampaignTransaction(ChargeCampaignRequest request, final long campaignId) {
        return this.handle.inTransaction(handle1 -> {
            //create transaction
            Money transactionMoney = Money.of(CurrencyUnit.of(request.getCurrencyCode()), request.getAmount());
            Transaction transaction = new Transaction(0, new Date(), transactionMoney.getCurrencyUnit().getCode(), transactionMoney.getAmount().doubleValue(), null, null, Operation.CHARGE, null);
            this.createTransaction(transaction);

            //create debit part

            Campaign campaign = this.getCampaignById(campaignId);
            campaign.setWallet(this.getWalletById(campaign.getWalletId()));
            CurrencyUnit campaignCurrency = CurrencyUnit.of(campaign.getWallet().getCurrencyCode());
            double amount = CurrencyExchangeUtil.convert(transactionMoney, campaignCurrency);

            TransactionPart debitPart = new TransactionPart(0, transaction.getId(), campaign.getWalletId(), TransactionDirection.DEBIT, amount, campaignCurrency.getCode());
            this.createTransactionPart(debitPart);
            transaction.addTransactionPart(debitPart);

            //create credit part
            Wallet customerWallet = this.getWalletById(request.getCustomerWalletId());
            CurrencyUnit customerCurrency = CurrencyUnit.of(customerWallet.getCurrencyCode());
            amount = CurrencyExchangeUtil.convert(transactionMoney, customerCurrency);
            TransactionPart creditPart = new TransactionPart(0, transaction.getId(), customerWallet.getId(), TransactionDirection.CREDIT, amount, customerCurrency.getCode());
            this.createTransactionPart(creditPart);
            transaction.addTransactionPart(creditPart);

            //Generate Response
            return transaction;
        });
    }

    public Transaction refundTransaction(long transactionId, String refundReason) {
        return this.handle.inTransaction(handle1 -> {
            Transaction transaction = this.getTransactionById(transactionId);
            transaction.getTransactionParts().addAll(this.getTransactionPartsByTransactionId(transaction.getId()));

            //create refund transaction
            Transaction refundTransaction = new Transaction(0,
                    new Date(),
                    transaction.getOriginalCurrencyCode(),
                    transaction.getOriginalAmount(),
                    null,
                    transaction.getId(),
                    Operation.REFUND, refundReason);
            this.createTransaction(refundTransaction);

            //create refund part
            for (TransactionPart part : transaction.getTransactionParts()) {
                TransactionPart refundPart = new TransactionPart(0,
                        refundTransaction.getId(),
                        part.getWalletId(),
                        part.getDirection().isDebit() ? TransactionDirection.CREDIT : TransactionDirection.DEBIT,
                        part.getAmount(), part.getCurrencyCode());
                this.createTransactionPart(refundPart);
                refundTransaction.addTransactionPart(refundPart);
            }
            return refundTransaction;
        });
    }
}
