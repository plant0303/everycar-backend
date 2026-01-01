package com.road_friends.everycar.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoleDTO {
    private Integer id; // 권한번호
    private String name; // 권한이름
}
