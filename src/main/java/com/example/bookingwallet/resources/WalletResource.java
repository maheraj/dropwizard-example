package com.example.bookingwallet.resources;

import com.example.bookingwallet.api.request.ChargeCustomerRequest;
import com.example.bookingwallet.api.request.CreateWalletRequest;
import com.example.bookingwallet.api.request.RefundCustomerRequest;
import com.example.bookingwallet.api.response.ChargeCustomerResponse;
import com.example.bookingwallet.api.response.RefundCustomerResponse;
import com.example.bookingwallet.api.response.WalletBalanceResponse;
import com.example.bookingwallet.api.response.WalletResponse;
import com.example.bookingwallet.core.Transaction;
import com.example.bookingwallet.core.TransactionPart;
import com.example.bookingwallet.core.Wallet;
import com.example.bookingwallet.core.constant.TransactionDirection;
import com.example.bookingwallet.core.constant.WalletType;
import com.example.bookingwallet.service.BookingWalletService;
import com.example.bookingwallet.util.CurrencyExchangeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("/wallets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Customer Wallet APIs")
public class WalletResource {

    private BookingWalletService bookingWalletService;

    public WalletResource(BookingWalletService service) {
        this.bookingWalletService = service;
    }

    @GET
    @ApiOperation(value = "List wallets", nickname = "wallets")
    public List<WalletResponse> wallets() {
        List<Wallet> walletList = bookingWalletService.listWallets();
        List<WalletResponse> results = new ArrayList<>();
        for (Wallet wallet : walletList) {
            results.add(new WalletResponse(wallet));
        }
        return results;
    }

    @GET
    @Path("/{walletId}")
    @ApiOperation(value = "Get customer wallet", nickname = "get wallet")
    public WalletResponse getWalletById(
            @ApiParam(value = "walletId", required = true)
            @Positive(message = "must be valid")
            @PathParam("walletId") long walletId) {
        Wallet wallet = bookingWalletService.getWalletById(walletId);
        return new WalletResponse(wallet);
    }

    @POST
    @ApiOperation(value = "Create customer wallet", nickname = "create wallet")
    public WalletResponse createCustomerWallet(@Valid final CreateWalletRequest request) {
        Wallet wallet = new Wallet();
        wallet.setWalletType(WalletType.CUSTOMER_WALLET);
        wallet.setCurrencyCode(request.getCurrencyCode());
        wallet.setCustomerId(request.getCustomerId());
        bookingWalletService.createWallet(wallet);
        return new WalletResponse(wallet);
    }

    @POST
    @Path("/{walletId}/charge")
    @ApiOperation(value = "Charge customer wallet", nickname = "charge wallet")
    public ChargeCustomerResponse chargeCustomer(
            @Valid final ChargeCustomerRequest request,

            @ApiParam(value = "walletId", required = true)
            @Positive(message = "must be valid")
            @PathParam("walletId") final long walletId
    ) throws Exception {
        //validations
        Wallet customerWallet = bookingWalletService.getWalletById(walletId);
        double amount = CurrencyExchangeUtil.convert(request.getCurrencyCode(), request.getAmount(), customerWallet.getCurrencyCode());
        double balance = bookingWalletService.getWalletBalance(customerWallet.getId());
        if (amount > balance) {
            throw new Exception("Insufficient balance");
        }


        Transaction transaction = new Transaction();
        transaction.setDate(new Date());
        bookingWalletService.createTransaction(transaction);

        TransactionPart debitPart = new TransactionPart(0, transaction.getId(), customerWallet.getId(), TransactionDirection.DEBIT, amount, customerWallet.getCurrencyCode());
        bookingWalletService.createTransactionPart(debitPart);
        transaction.addTransactionPart(debitPart);

        Wallet expenseWallet = bookingWalletService.getExpenseWallet();
        amount = CurrencyExchangeUtil.convert(request.getCurrencyCode(), request.getAmount(), expenseWallet.getCurrencyCode());
        TransactionPart creditPart = new TransactionPart(0, transaction.getId(), expenseWallet.getId(), TransactionDirection.CREDIT, amount, expenseWallet.getCurrencyCode());
        bookingWalletService.createTransactionPart(creditPart);
        transaction.addTransactionPart(creditPart);

        return new ChargeCustomerResponse(transaction);
    }

    @POST
    @Path("/{walletId}/refund")
    @ApiOperation(value = "Refund Customer", nickname = "refund")
    public RefundCustomerResponse refundCustomer(
            @Valid final RefundCustomerRequest request,

            @ApiParam(value = "walletId", required = true)
            @Positive(message = "must be valid")
            @PathParam("walletId") final long walletId

    )  {
        /*
        Transaction transaction = transactionDao.getById(request.getTransactionId());
        transaction.getTransactionParts().addAll(transactionPartDao.getByTransactionId(transaction.getId()));
        if (transaction.isRefunded()) {
            return new RefundCustomerResponse(transaction);
        }

        //create debit part
        List<TransactionPart> refundParts = new ArrayList<>();
        for (TransactionPart part : transaction.getTransactionParts()) {
            TransactionPart refundPart = (TransactionPart) part.clone();
            refundPart.setRefund(true);
            refundPart.setDirection(part.getDirection().isDebit() ? TransactionDirection.CREDIT : TransactionDirection.DEBIT);
            refundPart.setId(transactionPartDao.insert(refundPart));
            refundParts.add(refundPart);
        }
        transaction.getTransactionParts().addAll(refundParts);
        transaction.setRefunded(true);
        transactionDao.update(transaction);
        return new RefundCustomerResponse(transaction);

         */
        return null;
    }

    @GET
    @Path("/{walletId}/balance")
    @ApiOperation(value = "Get Wallet Balance", nickname = "wallet balance")
    public WalletBalanceResponse getWalletBalance(
            @ApiParam(value = "walletId", required = true)
            @Positive(message = "must be valid")
            @PathParam("walletId") final long walletId

    )  {
        Wallet wallet = bookingWalletService.getWalletById(walletId);
        double balance = bookingWalletService.getWalletBalance(walletId);
        return new WalletBalanceResponse(wallet, balance);
    }
}
