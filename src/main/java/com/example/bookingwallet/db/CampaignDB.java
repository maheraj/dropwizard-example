package com.example.bookingwallet.db;

import com.example.bookingwallet.core.Campaign;
import com.example.bookingwallet.core.Wallet;
import com.example.bookingwallet.core.constant.WalletType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampaignDB {
    private static Map<Long, Campaign> campaigns = new HashMap<>();
    public static long SEQUENCE = 0;

    public static long nextSequence() {
        return ++SEQUENCE;
    }

    public static Campaign getById(long id) {
        return campaigns.get(id);
    }


    public static List<Campaign> getAll() {
        List<Campaign> result = new ArrayList<Campaign>();
        for (Long key : campaigns.keySet()) {
            result.add(campaigns.get(key));
        }
        return result;
    }

    public static void createCampaign(Campaign campaign) {
        //create wallet
        Wallet wallet = new Wallet(WalletType.CAMPAIGN_WALLET, campaign.getCurrency());
        WalletDB.createWallet(wallet);

        //create campaign
        campaign.setId(nextSequence());
        campaign.setWallet(wallet);
        campaigns.put(campaign.getId(), campaign);
    }

    public static void removeCampaign(long id) {
        if (!campaigns.keySet().isEmpty()) {
            campaigns.remove(id);
        }
    }

    public static int getCount() {
        return campaigns.size();
    }


}
