package com.road_friends.everycar.reservation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModelDTO {

    @JsonProperty("model_id")
    private Integer modelId;

    @JsonProperty("model_brand")
    private String modelBrand;

    @JsonProperty("model_name")
    private String modelName;

    @JsonProperty("model_category")
    private String modelCategory;

    @JsonProperty("model_seate_num")
    private String modelSeateNum;

    @JsonProperty("model_transmission")
    private String modelTransmission;

    @JsonProperty("model_amount_hour")
    private Integer modelAmountHour;

    @JsonProperty("model_amount_day")
    private Integer modelAmountDay;

}