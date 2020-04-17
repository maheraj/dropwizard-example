package com.example.bookingwallet.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@ApiModel(value = "RefundCustomerRequest")
public class RefundCustomerRequest extends BaseRequest {

    @Positive(message = "must be valid")
    @ApiModelProperty(value = "Transaction Id", required = true)
    private long transactionId;

    @NotEmpty
    @Size(min = 0, max = 255, message = "Max refund reason length 255 character")
    @ApiModelProperty(value = "Refund Reason", required = true)
    private String refundReason;

    public RefundCustomerRequest(long transactionId, String refundReason) {
        this.transactionId = transactionId;
        this.refundReason = refundReason;
    }
}
