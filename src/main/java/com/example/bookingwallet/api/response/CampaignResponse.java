package com.example.bookingwallet.api.response;

import com.example.bookingwallet.core.Campaign;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CampaignResponse {
    private long campaignId;
    private long campaignWalletId;
    private String name;
    private String currencyCode;
    private double budget;
    private List<Link> links = new ArrayList<>();

    public CampaignResponse(Campaign campaign) {
        this.campaignId = campaign.getId();
        this.name = campaign.getName();
        this.currencyCode = campaign.getWallet().getCurrencyCode();
        this.campaignWalletId = campaign.getWallet().getId();
        this.budget = campaign.getBudget();
        this.links.add(new Link("self", "/campaigns/" + this.campaignId));
    }
}
