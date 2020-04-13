package com.example.bookingwallet.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "IssuingCreditRequest")
public class IssuingCreditRequest extends BaseRequest {

    @ApiModelProperty(value = "Campaign Id", required = true)
    private long campaignId;

    @ApiModelProperty(value = "Customer Wallet Id", required = true)
    private long walletId;

    @ApiModelProperty(value = "Currency Code", required = true)
    @NotEmpty
    private String currency;

    @ApiModelProperty(value = "Credit Amount", required = true)
    private double amount;

    public IssuingCreditRequest(long campaignId, long walletId, @NotEmpty String currency, double amount) {
        this.campaignId = campaignId;
        this.walletId = walletId;
        this.currency = currency;
        this.amount = amount;
    }
}
