package com.example.bookingwallet.resources;

import com.example.bookingwallet.api.request.ChargeCampaignRequest;
import com.example.bookingwallet.api.request.CreateCampaignRequest;
import com.example.bookingwallet.api.request.RefundCampaignRequest;
import com.example.bookingwallet.api.response.CampaignResponse;
import com.example.bookingwallet.api.response.ChargeCampaignResponse;
import com.example.bookingwallet.api.response.RefundCampaignResponse;
import com.example.bookingwallet.core.Campaign;
import com.example.bookingwallet.core.Transaction;
import com.example.bookingwallet.service.BookingWalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jdbi.v3.core.Jdbi;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/campaigns")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Campaign Wallet APIs")
public class CampaignResource {

    private Jdbi jdbi;

    public CampaignResource(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @GET
    @Path("/{campaignId}")
    @ApiOperation(value = "Get campaign details", nickname = "get campaign")
    public CampaignResponse getCampaignById(
            @ApiParam(value = "Campaign Id", required = true)
            @Positive(message = "must be valid")
            @PathParam("campaignId") final long campaignId
    ) throws Exception {
        BookingWalletService service = new BookingWalletService(this.jdbi.open());
        Campaign campaign = service.getCampaignById(campaignId);
        if (campaign == null) {
            throw new Exception("Campaign Not Found!");
        }
        campaign.setWallet(service.getWalletById(campaign.getWalletId()));
        return new CampaignResponse(campaign);
    }

    //method c
    @GET
    @Path("/test-transaction")
    @ApiOperation(value = "Test JDBI Transaction", nickname = "JDBI Transaction")
    public ChargeCampaignResponse testJDBITransaction() throws Exception {
        return jdbi.inTransaction(handle -> {
            BookingWalletService service = new BookingWalletService(handle);

            CreateCampaignRequest request = new CreateCampaignRequest("Campaign", "EUR", 100);
            Campaign campaign = service.saveCampaign(request);

            ChargeCampaignRequest chargeRequest = new ChargeCampaignRequest(3, "EUR", 5.0);
            Transaction transaction = service.saveChargeCampaignTransaction(chargeRequest, campaign.getId());
            if (transaction.getOriginalAmount() == 5) {
                throw new Exception("Break the transaction");
            }
            return new ChargeCampaignResponse(campaign.getId(), transaction);
        });
    }

    @POST
    @ApiOperation(value = "Create campaign wallet", nickname = "create campaign")
    public CampaignResponse crateCampaign(@Valid CreateCampaignRequest request) {
        return jdbi.inTransaction(handle -> {
            BookingWalletService service = new BookingWalletService(handle);
            return new CampaignResponse(service.saveCampaign(request));
        });
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
        return jdbi.inTransaction(handle -> {
            BookingWalletService service = new BookingWalletService(handle);
            return new ChargeCampaignResponse(campaignId, service.saveChargeCampaignTransaction(request, campaignId));
        });

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
        return this.jdbi.inTransaction(handle -> {
            BookingWalletService service = new BookingWalletService(this.jdbi.open());
            return new RefundCampaignResponse(campaignId, service.refundTransaction(request.getTransactionId(), null));
        });
    }
}
