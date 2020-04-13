package com.example.bookingwallet;

import com.example.bookingwallet.api.request.*;
import com.example.bookingwallet.api.response.CampaignResponse;
import com.example.bookingwallet.api.response.TransactionResponse;
import com.example.bookingwallet.api.response.WalletResponse;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DropwizardExtensionsSupport.class)
public class IntegrationTest {

    public static final DropwizardAppExtension<BookingWalletConfiguration> RULE = new DropwizardAppExtension<>(BookingWalletApplication.class, new BookingWalletConfiguration());


    @Test
    public void testApp() {
        Client client = RULE.client();
        // Step 1: create a campaign
        CreateCampaignRequest createCampaignRequest = new CreateCampaignRequest("Funding Wallet - 1", "USD", 100);
        Response response = client.target(
                String.format("http://localhost:%d/campaigns", RULE.getLocalPort()))
                .request()
                .post(Entity.entity(createCampaignRequest, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        response.bufferEntity();
        CampaignResponse campaignResponse = response.readEntity(CampaignResponse.class);
        System.out.println("Campaign Response:");
        System.out.println(response.readEntity(String.class));
        System.out.println("-------------------------------------------------");
        // Step 2: create a customer wallet
        //WARNING - customer object is missing. assuming that I have customer ID
        long customerId = 1001;
        CreateWalletRequest createWalletRequest = new CreateWalletRequest(customerId, "USD");
        response = client.target(
                String.format("http://localhost:%d/wallets", RULE.getLocalPort()))
                .request()
                .post(Entity.entity(createWalletRequest, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        response.bufferEntity();
        WalletResponse walletResponse = response.readEntity(WalletResponse.class);
        System.out.println("Wallet Response:");
        System.out.println(response.readEntity(String.class));
        System.out.println("-------------------------------------------------");

        // Step 3: issue credit to a customer
        IssuingCreditRequest issuingCreditRequest = new IssuingCreditRequest(campaignResponse.getCampaignId(), walletResponse.getWalletId(), walletResponse.getCurrency(), 10);
        response = client.target(
                String.format("http://localhost:%d/transactions/issue-credit", RULE.getLocalPort()))
                .request()
                .post(Entity.entity(issuingCreditRequest, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        response.bufferEntity();
        TransactionResponse issuingCreditResponse = response.readEntity(TransactionResponse.class);
        System.out.println("Issue Credit Response:");
        System.out.println(response.readEntity(String.class));
        System.out.println("-------------------------------------------------");

        // Step 4: redeem credit for a reservation
        RedeemCreditRequest redeemCreditRequest = new RedeemCreditRequest(walletResponse.getWalletId(), walletResponse.getCurrency(), 10);
        response = client.target(
                String.format("http://localhost:%d/transactions/redeem-credit", RULE.getLocalPort()))
                .request()
                .post(Entity.entity(redeemCreditRequest, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        response.bufferEntity();
        TransactionResponse redeemCreditResponse = response.readEntity(TransactionResponse.class);
        System.out.println("Redeem Credit Response:");
        System.out.println(response.readEntity(String.class));
        System.out.println("-------------------------------------------------");
        // Step 5: refund credit to the customer
        RefundCreditRequest refundCreditRequest = new RefundCreditRequest();
        response = client.target(
                String.format("http://localhost:%d/transactions/%d/refund", RULE.getLocalPort(), redeemCreditResponse.getTransactionId()))
                .request()
                .post(Entity.entity(redeemCreditRequest, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        response.bufferEntity();
        TransactionResponse refundCreditResponse = response.readEntity(TransactionResponse.class);
        System.out.println("Refund Customer Credit Response:");
        System.out.println(response.readEntity(String.class));
        System.out.println("-------------------------------------------------");
        // Step 6: refund credit to the campaign
        RefundCreditRequest refundCampaignRequest = new RefundCreditRequest();
        response = client.target(
                String.format("http://localhost:%d/transactions/%d/refund", RULE.getLocalPort(), issuingCreditResponse.getTransactionId()))
                .request()
                .post(Entity.entity(redeemCreditRequest, MediaType.APPLICATION_JSON));
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        response.bufferEntity();
        TransactionResponse refundCampaignResponse = response.readEntity(TransactionResponse.class);
        System.out.println("Refund Campaign Credit Response:");
        System.out.println(response.readEntity(String.class));
        System.out.println("-------------------------------------------------");
    }

}
