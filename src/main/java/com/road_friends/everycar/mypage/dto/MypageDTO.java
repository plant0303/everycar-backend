package com.road_friends.everycar.mypage.dto;

import com.road_friends.everycar.user.dto.RoleDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class MypageDTO {
    private Long userNum;
    private String userId;
    private String userName;
    private String userPassword;
    private String userEmail;
    private String userPhone;
    private int userGender;
    private LocalDate userBirth;
    private String userAddress;
    private int userStatus;
    private boolean enabled = true;
    private List<RoleDTO> roles;
}
