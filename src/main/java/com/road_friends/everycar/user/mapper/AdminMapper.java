package com.road_friends.everycar.user.mapper;

import com.road_friends.everycar.user.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
    UserDTO findByUserId(String userId);
}