package com.road_friends.everycar.admin.car.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CarAdminDTO {
    private Integer carId;
    private String modelId;
    private Integer carCategory;
    private Integer carStatus;
    private Integer carYear;
    private String carFuel;
    private String carGrade;
    private String carOptions;
    private Integer rentalStation;

    // 조인을 통해 가져올 데이터
    private String modelName;
    private String modelBrand;
    private String parkingName;

    // 수정 페이지용: 대여중일 때 예약 정보
    private String currentUserName;
    private LocalDateTime currentRentalDate;
}