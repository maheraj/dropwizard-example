package com.example.bookingwallet.resources;

import com.example.bookingwallet.api.request.ChargeCampaignRequest;
import com.example.bookingwallet.api.request.CreateCampaignRequest;
import com.example.bookingwallet.api.request.RefundCampaignRequest;
import com.example.bookingwallet.api.response.CampaignResponse;
import com.example.bookingwallet.api.response.ChargeCampaignResponse;
import com.example.bookingwallet.api.response.RefundCampaignResponse;
import com.example.bookingwallet.core.Campaign;
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
import java.util.Date;

@Path("/campaigns")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Campaign Wallet APIs")
public class CampaignResource {

    private BookingWalletService bookingWalletService;

    public CampaignResource(BookingWalletService service) {
        this.bookingWalletService = service;
    }


    @GET
    @Path("/{campaignId}")
    @ApiOperation(value = "Get campaign details", nickname = "get campaign")
    public CampaignResponse getCampaignById(
            @ApiParam(value = "Campaign Id", required = true)
            @Positive(message = "must be valid")
            @PathParam("campaignId") final long campaignId
    ) throws Exception {
        Campaign campaign = bookingWalletService.getCampaignById(campaignId);
        if (campaign == null) {
            throw new Exception("Campaign Not Found!");
        }
        campaign.setWallet(bookingWalletService.getWalletById(campaign.getWalletId()));

        return new CampaignResponse(campaign);
    }

    @POST
    @ApiOperation(value = "Create campaign wallet", nickname = "create campaign")
    public CampaignResponse crateCampaign(@Valid CreateCampaignRequest request) {
        //create campaign wallet
        Wallet wallet = new Wallet(0, request.getCurrencyCode(), WalletType.CAMPAIGN_WALLET, null);
        bookingWalletService.createWallet(wallet);
        //create campaign
        Campaign campaign = new Campaign(0, request.getName(), 0, request.getBudget(), wallet.getId(), null, wallet);
        bookingWalletService.createCampaign(campaign);
        return new CampaignResponse(campaign);
    }

    @POST
    @Path("/{campaignId}/charge")
    @ApiOperation(value = "Charge campaign wallet", nickname = "charge campaign")
    public ChargeCampaignResponse chargeCampaign(
            @Valid final ChargeCampaignRequest request,

            @ApiParam(value = "campaignId", required = true)
            @Positive(message = "must be valid")
            @PathParam("campaignId") final long campaignId

    ) {
        //validations

        //create transaction
        Transaction transaction = new Transaction(0, new Date(), request.getCurrencyCode(), request.getAmount(), null);
        bookingWalletService.createTransaction(transaction);

        //create debit part

        Campaign campaign = bookingWalletService.getCampaignById(campaignId);
        campaign.setWallet(bookingWalletService.getWalletById(campaign.getWalletId()));
        double amount = CurrencyExchangeUtil.convert(request.getCurrencyCode(), request.getAmount(), campaign.getWallet().getCurrencyCode());

        TransactionPart debitPart = new TransactionPart(0, transaction.getId(), campaign.getWallet().getId(), TransactionDirection.DEBIT, amount, campaign.getWallet().getCurrencyCode());
        bookingWalletService.createTransactionPart(debitPart);
        transaction.addTransactionPart(debitPart);

        //create credit part
        Wallet customerWallet = bookingWalletService.getWalletById(request.getCustomerWalletId());
        amount = CurrencyExchangeUtil.convert(request.getCurrencyCode(), request.getAmount(), customerWallet.getCurrencyCode());
        TransactionPart creditPart = new TransactionPart(0, transaction.getId(), customerWallet.getId(), TransactionDirection.CREDIT, amount, customerWallet.getCurrencyCode());
        bookingWalletService.createTransactionPart(creditPart);
        transaction.addTransactionPart(creditPart);

        //Generate Response
        return new ChargeCampaignResponse(campaignId, transaction);
    }

    @POST
    @Path("/{campaignId}/refund")
    @ApiOperation(value = "Refund campaign wallet", nickname = "refund campaign")
    public RefundCampaignResponse refundCampaign(
            @Valid final RefundCampaignRequest request,

            @ApiParam(value = "campaignId", required = true)
            @Positive(message = "must be valid")
            @PathParam("campaignId") final long campaignId

    ) {

        /*
        Transaction transaction = transactionDao.getById(request.getTransactionId());
        transaction.getTransactionParts().addAll(transactionPartDao.getByTransactionId(transaction.getId()));
        if (transaction.isRefunded()) {
            return new RefundCampaignResponse(campaignId, transaction);
        }

        //create debit part
        List<TransactionPart> refundParts = new ArrayList<>();
        for (TransactionPart part : transaction.getTransactionParts()) {
            TransactionPart refundPart = (TransactionPart) part.clone();

            refundPart.setDirection(part.getDirection().isDebit() ? TransactionDirection.CREDIT : TransactionDirection.DEBIT);
            refundPart.setId(transactionPartDao.insert(refundPart));
            refundParts.add(refundPart);
        }
        transaction.getTransactionParts().addAll(refundParts);
        transaction.setRefunded(true);
        transactionDao.update(transaction);
        return new RefundCampaignResponse(campaignId, transaction);
        */
        return null;
    }
}
