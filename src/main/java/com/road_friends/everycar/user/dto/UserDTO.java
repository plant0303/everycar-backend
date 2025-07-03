package com.road_friends.everycar.user.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
public class UserDTO {
    private Long userNum;
    private String userId;
    private String userName;
    private String userPassword;
    private String userEmail;
    private String userPhone;
    private int userGender;
    private Date userBirth;
    private String userAddress;
    private int userStatus;
    private boolean enabled;
}
