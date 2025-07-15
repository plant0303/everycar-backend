package com.road_friends.everycar.reservation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarDTO {

    @JsonProperty("car_id")
    private Integer carId;  // car_id -> carId

    @JsonProperty("model_id")
    private String modelId;

    @JsonProperty("car_category")
    private String carCategory;

    @JsonProperty("car_status")
    private Integer carStatus;

    @JsonProperty("car_year")
    private Integer carYear;

    @JsonProperty("car_fuel")
    private String carFuel;

    @JsonProperty("car_grade")
    private String carGrade;

    @JsonProperty("car_options")
    private String carOptions;

    @JsonProperty("rental_station")
    private Integer rentalStation;

    private Long totalPrice;

    private ModelDTO model;

    private ParkingDTO parking;

    private List<ParkingDTO> parkingList;
}
