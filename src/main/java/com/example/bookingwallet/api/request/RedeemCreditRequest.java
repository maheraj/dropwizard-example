package com.example.bookingwallet.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "RedeemCreditRequest")
public class RedeemCreditRequest extends BaseRequest {

    @ApiModelProperty(value = "Customer Wallet Id", required = true)
    private long walletId;

    @ApiModelProperty(value = "Currency Code", required = true)
    @NotEmpty
    private String currency;

    @ApiModelProperty(value = "Credit Amount", required = true)
    private double amount;

    public RedeemCreditRequest(long walletId, @NotEmpty String currency, double amount) {
        this.walletId = walletId;
        this.currency = currency;
        this.amount = amount;
    }
}
