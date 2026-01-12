package com.road_friends.everycar.mypage.dto;

import lombok.Data;

@Data
public class ReservationDetailDTO {
    private Long reservationId;
    private String paymentId;
    private Integer carId;
    private Long userNum;
    private String rentalDate;
    private String returnDate;
    private Integer totalPrice;
    private String status;
    private String createdAt;

    // 조인 데이터
    private String carName;
    private String brand;
    private String fuel;
    private String carImg;
    private String parkingName;
    private String parkingAddress;
}