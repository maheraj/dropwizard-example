package com.example.bookingwallet.resources;

import com.example.bookingwallet.api.request.IssuingCreditRequest;
import com.example.bookingwallet.api.request.RedeemCreditRequest;
import com.example.bookingwallet.api.request.RefundCreditRequest;
import com.example.bookingwallet.api.response.TransactionResponse;
import com.example.bookingwallet.core.Campaign;
import com.example.bookingwallet.core.Transaction;
import com.example.bookingwallet.core.TransactionPart;
import com.example.bookingwallet.core.Wallet;
import com.example.bookingwallet.core.constant.TransactionDirection;
import com.example.bookingwallet.db.CampaignDB;
import com.example.bookingwallet.db.TransactionDB;
import com.example.bookingwallet.db.WalletDB;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Api("Transaction APIs")
public class TransactionResource {

    @GET
    @Path("/{transactionId}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 404, message = "Transaction is not found"),
            @ApiResponse(code = 500, message = "Server internal error")
    })
    @ApiOperation(value = "This api will return transaction details", response = TransactionResponse.class)
    public Response getTransactionById(@PathParam("transactionId") long transactionId) {
        Transaction transaction = TransactionDB.getById(transactionId);
        if (transaction != null) {
            return Response.ok(new TransactionResponse(transaction)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/issue-credit")
    @ApiOperation(value = "This api will move fund from campaign to customer", response = TransactionResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "Server failed to create")
    })
    public Response issueCredit(@NotNull @Valid IssuingCreditRequest request) {
        Campaign campaign = CampaignDB.getById(request.getCampaignId());
        Wallet wallet = WalletDB.getById(request.getWalletId());

        //create transaction
        Transaction transaction = new Transaction();
        transaction.setDate(new Date());

        //create debit part
        TransactionPart debitPart = new TransactionPart();
        debitPart.setWalletId(campaign.getWallet().getId());
        debitPart.setDirection(TransactionDirection.DEBIT);
        debitPart.setCurrency(request.getCurrency());
        debitPart.setAmount(request.getAmount());
        transaction.getTransactionParts().add(debitPart);

        //create credit part
        TransactionPart creditPart = new TransactionPart();
        creditPart.setWalletId(wallet.getId());
        creditPart.setDirection(TransactionDirection.CREDIT);
        creditPart.setCurrency(request.getCurrency());
        creditPart.setAmount(request.getAmount());
        transaction.getTransactionParts().add(creditPart);

        //save transaction
        TransactionDB.createTransaction(transaction);

        //Generate Response
        TransactionResponse response = new TransactionResponse(transaction);
        return Response.ok(response).build();
    }

    @POST
    @Path("/redeem-credit")
    @ApiOperation(value = "This api will make payment from customer", response = TransactionResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "Server failed to create")
    })
    public Response redeemCredit(@NotNull @Valid RedeemCreditRequest request) {
        Wallet expenseWallet = WalletDB.getById(1);//default expense wallet id 1
        Wallet wallet = WalletDB.getById(request.getWalletId());

        //create transaction
        Transaction transaction = new Transaction();
        transaction.setDate(new Date());

        //create debit part
        TransactionPart debitPart = new TransactionPart();
        debitPart.setWalletId(wallet.getId());
        debitPart.setDirection(TransactionDirection.DEBIT);
        debitPart.setCurrency(request.getCurrency());
        debitPart.setAmount(request.getAmount());
        transaction.getTransactionParts().add(debitPart);

        //create credit part
        TransactionPart creditPart = new TransactionPart();
        creditPart.setWalletId(expenseWallet.getId());
        creditPart.setDirection(TransactionDirection.CREDIT);
        creditPart.setCurrency(request.getCurrency());
        creditPart.setAmount(request.getAmount());
        transaction.getTransactionParts().add(creditPart);

        //save transaction
        TransactionDB.createTransaction(transaction);

        //Generate Response
        TransactionResponse response = new TransactionResponse(transaction);
        return Response.ok(response).build();
    }

    @POST
    @Path("/{transactionId}/refund")
    @ApiOperation(value = "This api will refund the transaction", response = TransactionResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "Server failed to create")
    })
    public Response refundCredit(@PathParam("transactionId") long transactionId, @NotNull @Valid RefundCreditRequest request) throws CloneNotSupportedException {
        Transaction transaction = TransactionDB.getById(transactionId);
        if (transaction == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (transaction.isRefunded()) {
            return Response.ok(new TransactionResponse(transaction)).build();
        }

        //create debit part
        List<TransactionPart> refundParts = new ArrayList<>();
        for (TransactionPart part : transaction.getTransactionParts()) {
            TransactionPart refundPart = (TransactionPart) part.clone();
            refundPart.setRefund(true);
            refundPart.setDirection(part.getDirection().isDebit() ? TransactionDirection.CREDIT : TransactionDirection.DEBIT);
            TransactionDB.createTransactionParts(refundPart);
            refundParts.add(refundPart);
        }
        transaction.getTransactionParts().addAll(refundParts);
        transaction.setRefunded(true);
        return Response.ok(new TransactionResponse(transaction)).build();
    }
}
