package com.example.bookingwallet.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@ApiModel(value = "RefundCampaignRequest")
public class RefundCampaignRequest extends BaseRequest {

    @Positive(message = "must be valid")
    @ApiModelProperty(value = "Transaction Id", required = true)
    private long transactionId;

    public RefundCampaignRequest(long transactionId) {
        this.transactionId = transactionId;
    }
}
