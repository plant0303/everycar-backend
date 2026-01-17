package com.road_friends.everycar.admin.reservation.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ReservationDTO {
    // 예약 정보
    private Long reservationId;
    private String paymentId;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime rentalDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime returnDate;

    private Integer totalPrice;
    private String status;
    private LocalDateTime createdAt;

    // 유저 정보
    private Long userNum;
    private String userId;
    private String userName;

    // 차량 및 장소 정보
    private Integer carId;
    private String modelName;   // model 테이블
    private String modelBrand;  // model 테이블
    private String parkingName; // parking 테이블
}