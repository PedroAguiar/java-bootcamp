package com.globant.controller.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "PaymentResponse", description = "Payment Response")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PaymentResponse {

    @ApiModelProperty(value = "payment id")
    private String paymentId;
}
