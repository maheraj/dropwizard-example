package com.example.bookingwallet.resources;

import com.example.bookingwallet.api.request.CreateCampaignRequest;
import com.example.bookingwallet.api.request.CreateWalletRequest;
import com.example.bookingwallet.api.response.WalletResponse;
import com.example.bookingwallet.core.Wallet;
import com.example.bookingwallet.core.constant.WalletType;
import com.example.bookingwallet.db.WalletDB;
import io.swagger.annotations.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/wallets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Customer Wallet APIs")
public class CustomerResource {


    @GET
    @ApiOperation(value = "This will return wallet details", response = WalletResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "Server failed to create")
    })
    public Response getWalletByCustomerId(@QueryParam("customerId") long customerId) {
        Wallet wallet = WalletDB.getByCustomerId(customerId);
        if (wallet != null) {
            return Response.ok(new WalletResponse(wallet)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{walletId}")
    @ApiOperation(value = "This will return wallet details", response = WalletResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "Server failed to create")
    })
    public Response getWalletById(@PathParam("walletId") long walletId) {
        Wallet wallet = WalletDB.getById(walletId);
        if (wallet != null) {
            return Response.ok(new WalletResponse(wallet)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @ApiOperation(value = "This will create customer wallet", response = WalletResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 500, message = "Server failed to create")
    })
    public Response createWallet(@NotNull @Valid CreateWalletRequest request) {
        Wallet wallet = new Wallet();
        wallet.setWalletType(WalletType.CUSTOMER_WALLET);
        wallet.setCurrency(request.getCurrency());
        wallet.setCustomerId(request.getCustomerId());
        WalletDB.createWallet(wallet);
        return Response.ok(new WalletResponse(wallet)).build();
    }
}
