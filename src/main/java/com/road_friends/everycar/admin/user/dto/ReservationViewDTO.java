package com.road_friends.everycar.admin.user.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationViewDTO {
    private Long reservationId;
    private LocalDateTime rentalDate;
    private LocalDateTime returnDate;
    private Integer totalPrice;
    private String status;
    private String parkingName;
    private String carModel;
}