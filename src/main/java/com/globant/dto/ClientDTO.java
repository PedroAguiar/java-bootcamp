package com.globant.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Api(value = "/client")
@Data
@Builder
@ApiModel(value = "Client")
public class ClientDTO {

    @ApiModelProperty(value = "client id")
    private String clientId;
    @ApiModelProperty(value = "client name")
    private String name;
    @ApiModelProperty(value = "client last name")
    private String lastName;
    @ApiModelProperty(value = "client description")
    private String description;
    @ApiModelProperty(value = "client orders per payment")
    private List<String> paymentIds;

}
