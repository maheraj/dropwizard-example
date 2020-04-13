package com.example.bookingwallet.api.response;

import com.example.bookingwallet.core.Campaign;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ApiModel("Campaign")
public class CampaignResponse {
    private long campaignId;
    private String name;
    private String currency;
    private double balance;
    private double budget;
    private Long lastTransactionId;
    private List<Link> links = new ArrayList<>();

    public CampaignResponse(Campaign campaign) {
        this.campaignId = campaign.getId();
        this.name = campaign.getName();
        this.currency = campaign.getWallet().getCurrency();
        this.balance = campaign.getBalance();
        this.budget = campaign.getBudget();
        this.links.add(new Link("self", "/campaigns/" + this.campaignId));
    }
}
