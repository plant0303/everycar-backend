package com.road_friends.everycar.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LogoutRequest {
    private String userId;
}
