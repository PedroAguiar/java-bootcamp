package com.globant.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Payment", description = "Payment DTO")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PaymentDTO {

    @ApiModelProperty(value = "payment id")
    private String id;
    @ApiModelProperty(value = "payment order id")
    private String orderId;
    @ApiModelProperty(value = "payment amount")
    private String amount;

}
