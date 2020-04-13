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
@ApiModel(value = "RefundCreditRequest")
public class RefundCreditRequest extends BaseRequest {

}
