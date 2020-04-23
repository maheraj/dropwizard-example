package com.example.bookingwallet;

import com.example.bookingwallet.api.request.ChargeCampaignRequest;
import com.example.bookingwallet.api.request.ChargeCustomerRequest;
import com.example.bookingwallet.api.request.CreateCampaignRequest;
import com.example.bookingwallet.api.request.CreateWalletRequest;
import com.example.bookingwallet.api.response.*;
import com.example.bookingwallet.service.BookingWalletService;
import com.example.bookingwallet.util.CurrencyExchangeUtil;
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
        CurrencyUnit campaignCurrency = CurrencyUnit.EUR;
        Money budgetMoney = Money.of(campaignCurrency, 100);
        CreateCampaignRequest createCampaignRequest = new CreateCampaignRequest("Funding Campaign", campaignCurrency.getCode(), budgetMoney.getAmount().doubleValue());
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
        CurrencyUnit customerCurrency = CurrencyUnit.of("BHD");
        long customerId = 1001;
        CreateWalletRequest createWalletRequest = new CreateWalletRequest(customerId, customerCurrency.getCode());
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
        //CurrencyUnit transactionCurrency = CurrencyUnit.CAD;
        CurrencyUnit transactionCurrency = CurrencyUnit.of("CAD");
        Money transactionMoney = Money.of(transactionCurrency, 15);
        ChargeCampaignRequest chargeCampaignRequest = new ChargeCampaignRequest(walletResponse.getWalletId(), transactionMoney.getCurrencyUnit().getCode(), transactionMoney.getAmount().doubleValue());
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

        // Step 5: get customer wallet balance
        response = client.target(
                String.format("http://localhost:%d/booking-wallet/wallets/%d/balance", RULE.getLocalPort(), walletResponse.getWalletId()))
                .request()
                .get();

        System.out.println("Customer Wallet balance Response:");
        response.bufferEntity();
        System.out.println(response.readEntity(String.class));
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        WalletBalanceResponse walletBalance = response.readEntity(WalletBalanceResponse.class);
        System.out.println("-------------------------------------------------");

        Money walletMoney = Money.of(CurrencyUnit.of(walletBalance.getCurrencyCode()), walletBalance.getBalance());
        assertThat(CurrencyExchangeUtil.convert(walletMoney, campaignCurrency) + campaignBalance.getBalance()).isEqualTo(0);

        // Step 5: get expense wallet balance
        response = client.target(
                String.format("http://localhost:%d/booking-wallet/wallets/%d/balance", RULE.getLocalPort(), BookingWalletService.EXPENSE_WALLET_ID))
                .request()
                .get();

        System.out.println("Expense Wallet balance Response:");
        response.bufferEntity();
        System.out.println(response.readEntity(String.class));
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        WalletBalanceResponse expenseWalletBalance = response.readEntity(WalletBalanceResponse.class);
        Money expenseBalanceMoney = Money.of(CurrencyUnit.of(expenseWalletBalance.getCurrencyCode()), expenseWalletBalance.getBalance());
        System.out.println("-------------------------------------------------");

        // Step 3: charge customer wallet
        ChargeCustomerRequest chargeCustomerRequest = new ChargeCustomerRequest(transactionMoney.getCurrencyUnit().getCode(), transactionMoney.getAmount().doubleValue());
        response = client.target(
                String.format("http://localhost:%d/booking-wallet/wallets/%d/charge", RULE.getLocalPort(), walletResponse.getWalletId()))
                .request()
                .post(Entity.entity(chargeCustomerRequest, MediaType.APPLICATION_JSON));

        response.bufferEntity();
        System.out.println("Charge Customer Response:");
        System.out.println(response.readEntity(String.class));
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        response.readEntity(ChargeCustomerResponse.class);
        System.out.println("-------------------------------------------------");

        // Step 5: get customer wallet balance
        response = client.target(
                String.format("http://localhost:%d/booking-wallet/wallets/%d/balance", RULE.getLocalPort(), walletResponse.getWalletId()))
                .request()
                .get();

        System.out.println("Customer Wallet balance Response:");
        response.bufferEntity();
        System.out.println(response.readEntity(String.class));
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        walletBalance = response.readEntity(WalletBalanceResponse.class);
        Money walletBalanceMoneyAfterRedeem = Money.of(CurrencyUnit.of(walletBalance.getCurrencyCode()), walletBalance.getBalance());
        System.out.println("-------------------------------------------------");
        assertThat(walletBalance.getBalance()).isEqualTo(0);

        // Step 5: get expense wallet balance
        response = client.target(
                String.format("http://localhost:%d/booking-wallet/wallets/%d/balance", RULE.getLocalPort(), BookingWalletService.EXPENSE_WALLET_ID))
                .request()
                .get();

        System.out.println("Expense Wallet balance Response:");
        response.bufferEntity();
        System.out.println(response.readEntity(String.class));
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        WalletBalanceResponse updatedExpenseWalletBalance = response.readEntity(WalletBalanceResponse.class);
        Money updatedExpenseBalanceMoney = Money.of(CurrencyUnit.of(updatedExpenseWalletBalance.getCurrencyCode()), updatedExpenseWalletBalance.getBalance());
        System.out.println("-------------------------------------------------");

        Money diff = Money.of(updatedExpenseBalanceMoney.getCurrencyUnit(), updatedExpenseBalanceMoney.getAmount().subtract(expenseBalanceMoney.getAmount()).doubleValue());

        assertThat(CurrencyExchangeUtil.convert(diff, transactionCurrency) - transactionMoney.getAmount().doubleValue()).isEqualTo(0);

    }

}
