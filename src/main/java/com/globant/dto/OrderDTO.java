package com.globant.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Order", description = "Order DTO")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrderDTO {

    @ApiModelProperty(value = "order id")
    private String orderId;
    @ApiModelProperty(value = "order item ids")
    private List<String> itemIds;

}
