package com.example.bookingwallet.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@ApiModel(value = "CreateCampaignRequest")
public class CreateCampaignRequest extends BaseRequest {

    @NotEmpty
    @Size(min = 3, max = 16, message = "must not exceed  16 characters")
    @ApiModelProperty(value = "Name of the campaign", required = true)
    private String name;

    @NotEmpty
    @Size(min = 3, max = 3, message = "must be 3 characters long")
    @ApiModelProperty(value = "Currency code: USD, BDT, etc.", required = true)
    private String currencyCode;

    @Positive(message = "must be greater than zero")
    @ApiModelProperty(value = "Campaign Budget", required = true)
    private double budget;

    public CreateCampaignRequest(String name, String currencyCode, double budget) {
        this.name = name;
        this.currencyCode = currencyCode;
        this.budget = budget;
    }
}
