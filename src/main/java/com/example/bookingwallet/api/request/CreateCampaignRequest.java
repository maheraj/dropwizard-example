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
@ApiModel(value = "CreateCampaignRequest")
public class CreateCampaignRequest extends BaseRequest {
    @ApiModelProperty(value = "Name of the campaign", required = true)
    @NotEmpty
    private String name;

    @ApiModelProperty(value = "Currency code", required = true)
    @NotEmpty
    private String currency;

    @ApiModelProperty(value = "Funding Amount", required = true)
    private double budget;

    public CreateCampaignRequest(@NotEmpty String name, @NotEmpty String currency, double budget) {
        this.name = name;
        this.currency = currency;
        this.budget = budget;
    }
}
