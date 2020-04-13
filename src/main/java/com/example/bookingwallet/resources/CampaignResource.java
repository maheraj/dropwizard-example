package com.example.bookingwallet.resources;

import com.example.bookingwallet.api.request.CreateCampaignRequest;
import com.example.bookingwallet.api.response.CampaignResponse;
import com.example.bookingwallet.core.Campaign;
import com.example.bookingwallet.core.Wallet;
import com.example.bookingwallet.core.constant.WalletType;
import com.example.bookingwallet.db.CampaignDB;
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
import java.net.URISyntaxException;

@Path("/campaigns")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Campaign APIs")
public class CampaignResource {

    @GET
    @Path("/{campaignId}")
    @ApiOperation(value = "This API will return campaign details", response = CampaignResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "Server failed to create")
    })
    public Response getCampaignById(@PathParam("campaignId") long campaignId) {
        Campaign campaign = CampaignDB.getById(campaignId);
        if (campaign != null) {
            return Response.ok(new CampaignResponse(campaign)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @ApiOperation(value = "This will create a new campaign", response = CampaignResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "Server failed to create")
    })
    public CampaignResponse crateCampaign(@NotNull @Valid CreateCampaignRequest request) throws URISyntaxException {
        Campaign campaign = new Campaign(request.getName(), request.getCurrency(), request.getBudget());
        CampaignDB.createCampaign(campaign);
        return new CampaignResponse(campaign);
    }
}
