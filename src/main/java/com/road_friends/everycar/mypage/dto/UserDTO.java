package com.road_friends.everycar.mypage.dto;

import lombok.Data;

import java.time.LocalDate;

// UserDTO.java
@Data
public class UserDTO {
    private Long userNum;
    private String userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private Integer userGender;
    private LocalDate userBirth;
    private String userAddress;

    // 면허증 정보 (동시 업데이트 처리를 위해 포함)
    private String licenseType;
    private String licenseNumber;
    private LocalDate licenseExpiry;
}