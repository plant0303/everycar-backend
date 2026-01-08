package com.road_friends.everycar.mypage.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserLicenseDTO {
    private Integer licenseId;        // PK
    private String licenseType;       // 면허 종류 (기본값: 1종 보통)
    private String licenseNum;        // 면허 번호 (Unique)
    private LocalDate licenseBirth;   // 면허상 생년월일
    private LocalDate licenseDate;    // 면허 발급일
    private LocalDate licenseEndDate; // 적성검사 만료일
    private String licenseRegion;     // 발급 지역
    private Long userNum;             // FK (user 테이블 참조)
    private String licensePhotoPath;  // 사진 경로

    // 시스템 관리용 (조회 전용)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}