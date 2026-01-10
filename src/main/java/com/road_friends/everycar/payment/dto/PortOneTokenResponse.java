package com.road_friends.everycar.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PortOneTokenResponse {
    @JsonProperty("accessToken")
    private String accessToken;
}