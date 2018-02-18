package com.globant.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Api(value = "/client")
@Data
@Builder
@ApiModel(value = "Client", description = "Client DTO")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ClientDTO {

    @ApiModelProperty(value = "client id")
    private String clientId;
    @ApiModelProperty(value = "client name")
    private String name;
    @ApiModelProperty(value = "client last name")
    private String lastName;
    @ApiModelProperty(value = "client description")
    private String description;
    @ApiModelProperty(value = "client orders payment ids")
    private List<String> paymentIds;

}
