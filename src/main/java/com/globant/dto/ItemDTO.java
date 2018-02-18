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
@ApiModel(value = "Item", description = "Item DTO")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ItemDTO {

    @ApiModelProperty(value = "item id")
    private String id;
    @ApiModelProperty(value = "item name")
    private String name;

}
