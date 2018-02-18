package com.globant.controller.api;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @JsonProperty(required = false)
    private String clientId;
    @JsonProperty(required = false)
    private String clientLastName;
    @JsonProperty(required = false)
    private String clientName;
    @JsonProperty(required = false)
    private String clientDescription;
    @JsonProperty(required = false)
    private String paymentAmount;
    @JsonProperty(required = false)
    private List<String> itemNames;



}
