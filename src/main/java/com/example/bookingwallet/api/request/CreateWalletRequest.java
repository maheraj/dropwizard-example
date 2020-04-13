package com.example.bookingwallet.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(value = "CreateWalletRequest")
public class CreateWalletRequest extends BaseRequest {

    @ApiModelProperty(value = "Currency code", required = true)
    @NotNull
    private Long customerId;

    @ApiModelProperty(value = "Currency code", required = true)
    @NotEmpty
    private String currency;

    public CreateWalletRequest(@NotNull Long customerId, @NotEmpty String currency) {
        this.customerId = customerId;
        this.currency = currency;
    }
}
