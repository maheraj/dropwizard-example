package com.example.bookingwallet.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@ApiModel(value = "ChargeCustomerRequest")
public class ChargeCustomerRequest extends BaseRequest {

    @NotEmpty
    @Size(min = 3, max = 3, message = "must be 3 characters long")
    @ApiModelProperty(value = "Currency code: USD, BDT, etc.", required = true)
    private String CurrencyCode;

    @Positive(message = "must be greater than zero")
    @ApiModelProperty(value = "Credit Amount", required = true)
    private double amount;

    public ChargeCustomerRequest(String currencyCode, double amount) {
        CurrencyCode = currencyCode;
        this.amount = amount;
    }
}
