package com.road_friends.everycar.reservation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParkingDTO {

    @JsonProperty("parking_id")
    private Integer parkingId;

    @JsonProperty("parking_name")
    private String parkingName;

    @JsonProperty("parking_address")
    private String parkingAddress;

    @JsonProperty("parking_latitude")
    private BigDecimal parkingLatitude;

    @JsonProperty("parking_longitude")
    private BigDecimal parkingLongitude;

    @JsonProperty("parking_province")
    private String parkingProvince;

    @JsonProperty("parking_district")
    private String parkingDistrict;
}
