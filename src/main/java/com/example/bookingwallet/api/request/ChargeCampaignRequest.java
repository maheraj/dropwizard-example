package com.example.bookingwallet.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@ApiModel(value = "ChargeCampaignRequest")
public class ChargeCampaignRequest extends BaseRequest {

    @Positive(message = "must be valid")
    @ApiModelProperty(value = "Customer Wallet Id", required = true)
    private long customerWalletId;

    @NotEmpty
    @Size(min = 3, max = 3, message = "must be 3 characters long")
    @ApiModelProperty(value = "Currency code: USD, BDT, etc.", required = true)
    private String currencyCode;

    @Positive(message = "must be greater than zero")
    @ApiModelProperty(value = "Credit Amount", required = true)
    private double amount;

    public ChargeCampaignRequest(long customerWalletId, String currencyCode, double amount) {
        this.customerWalletId = customerWalletId;
        this.currencyCode = currencyCode;
        this.amount = amount;
    }
}
