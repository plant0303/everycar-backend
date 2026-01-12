package com.road_friends.everycar.mypage.dto;

import lombok.Data;

@Data
public class ReservationResponseDTO {
    private Long reservationId;
    private String paymentId;
    private Integer carId;
    private Long userNum;
    private String rentalDate;  // 프론트 출력을 위해 String 파싱 권장
    private String returnDate;
    private Integer totalPrice;
    private String status;

    // 조인 시 필요한 차량 정보 필드 추가
    private String carName;     // 모델명
    private String brand;       // 브랜드명
    private String fuel;        // 연료
    private String parkingName; // 주차장명
    private String carImg;      // 차량 이미지 URL
}