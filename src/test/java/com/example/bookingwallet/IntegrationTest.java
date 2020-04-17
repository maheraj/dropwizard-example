package com.example.bookingwallet;

import com.example.bookingwallet.api.request.ChargeCampaignRequest;
import com.example.bookingwallet.api.request.CreateCampaignRequest;
import com.example.bookingwallet.api.request.CreateWalletRequest;
import com.example.bookingwallet.api.response.CampaignResponse;
import com.example.bookingwallet.api.response.ChargeCampaignResponse;
import com.example.bookingwallet.api.response.WalletBalanceResponse;
import com.example.bookingwallet.api.response.WalletResponse;
import com.example.bookingwallet.util.CurrencyExchangeUtil;
import com.example.bookingwallet.util.EuroCurrencyRate;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(DropwizardExtensionsSupport.class)
class IntegrationTest {

    private static final DropwizardAppExtension<BookingWalletConfiguration> RULE = new DropwizardAppExtension<>(
            BookingWalletApplication.class,
            ResourceHelpers.resourceFilePath("config.yml"));

    @Test
    void testApp() {
        Client client = RULE.client();

        // Step 1: create a campaign
        Money budgetMoney = Money.of(CurrencyUnit.EUR, 100);
        CreateCampaignRequest createCampaignRequest = new CreateCampaignRequest("Funding Campaign", EuroCurrencyRate.EUR.name(), budgetMoney.getAmount().doubleValue());
        Response response = client.target(
                String.format("http://localhost:%d/booking-wallet/campaigns", RULE.getLocalPort()))
                .request()
                .post(Entity.entity(createCampaignRequest, MediaType.APPLICATION_JSON));

        response.bufferEntity();
        System.out.println("Create Campaign Response:");
        System.out.println(response.readEntity(String.class));
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        CampaignResponse campaignResponse = response.readEntity(CampaignResponse.class);
        System.out.println("-------------------------------------------------");

        // Step 2: create a customer wallet
        CurrencyUnit customerCurrencyUnit = CurrencyUnit.USD;
        long customerId = 1001;
        CreateWalletRequest createWalletRequest = new CreateWalletRequest(customerId, customerCurrencyUnit.getCode());
        response = client.target(
                String.format("http://localhost:%d/booking-wallet/wallets", RULE.getLocalPort()))
                .request()
                .post(Entity.entity(createWalletRequest, MediaType.APPLICATION_JSON));

        response.bufferEntity();
        System.out.println("Create Customer Wallet Response:");
        System.out.println(response.readEntity(String.class));
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        WalletResponse walletResponse = response.readEntity(WalletResponse.class);
        System.out.println("-------------------------------------------------");

        // Step 3: issue credit to a customer
        Money chargeCampaignMoney = Money.of(CurrencyUnit.CAD, 15);
        ChargeCampaignRequest chargeCampaignRequest = new ChargeCampaignRequest(walletResponse.getWalletId(), chargeCampaignMoney.getCurrencyUnit().getCode(), chargeCampaignMoney.getAmount().doubleValue());
        response = client.target(
                String.format("http://localhost:%d/booking-wallet/campaigns/%d/charge", RULE.getLocalPort(), campaignResponse.getCampaignId()))
                .request()
                .post(Entity.entity(chargeCampaignRequest, MediaType.APPLICATION_JSON));

        response.bufferEntity();
        System.out.println("Charge Campaign Response:");
        System.out.println(response.readEntity(String.class));
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        response.readEntity(ChargeCampaignResponse.class);
        System.out.println("-------------------------------------------------");

        // Step 4: get campaign balance
        response = client.target(
                String.format("http://localhost:%d/booking-wallet/wallets/%d/balance", RULE.getLocalPort(), campaignResponse.getCampaignWalletId()))
                .request()
                .get();
        System.out.println("Campaign balance Response:");
        response.bufferEntity();
        System.out.println(response.readEntity(String.class));
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        WalletBalanceResponse campaignBalance = response.readEntity(WalletBalanceResponse.class);
        System.out.println("-------------------------------------------------");

        // Step 5: get customer wallet response
        response = client.target(
                String.format("http://localhost:%d/booking-wallet/wallets/%d/balance", RULE.getLocalPort(), walletResponse.getWalletId()))
                .request()
                .get();

        System.out.println("Campaign balance Response:");
        response.bufferEntity();
        System.out.println(response.readEntity(String.class));
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        WalletBalanceResponse walletBalance = response.readEntity(WalletBalanceResponse.class);
        System.out.println("-------------------------------------------------");

        assertThat(CurrencyExchangeUtil.convert(walletBalance.getCurrencyCode(), walletBalance.getBalance(), campaignBalance.getCurrencyCode()) + campaignBalance.getBalance()).isEqualTo(0);

    }

}
