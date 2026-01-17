package com.road_friends.everycar.payment.dto;

import lombok.Data;

@Data
public class PaymentCheckRequest {
    private String paymentId;
    private int totalPrice; // 검증할 실제 금액
    private Long carId;
    private Long userNum;
    private String rentalDatetime;
    private String returnDatetime;
    private Integer parkingId;
}