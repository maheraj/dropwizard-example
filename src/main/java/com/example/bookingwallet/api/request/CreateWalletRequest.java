package com.example.bookingwallet.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@ApiModel(value = "CreateWalletRequest")
public class CreateWalletRequest extends BaseRequest {

    @Positive(message = "must be valid")
    @ApiModelProperty(value = "Customer Id", required = true)
    private long customerId;

    @NotEmpty
    @Size(min = 3, max = 3, message = "must be 3 characters long")
    @ApiModelProperty(value = "Currency code: USD, BDT, etc.", required = true)
    private String CurrencyCode;

    public CreateWalletRequest(long customerId, String currencyCode) {
        this.customerId = customerId;
        CurrencyCode = currencyCode;
    }
}
