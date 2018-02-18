package com.globant.controller.api;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "PaymentRequest", description = "Payment Request")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PaymentRequest {

    @ApiModelProperty(value = "client id")
    private String clientId;
    @ApiModelProperty(value = "client last name")
    private String clientLastName;
    @ApiModelProperty(value = "client name")
    private String clientName;
    @ApiModelProperty(value = "client description")
    private String clientDescription;
    @ApiModelProperty(value = "payment amount")
    private String paymentAmount;
    @ApiModelProperty(value = "item names")
    private List<String> itemNames;

}
