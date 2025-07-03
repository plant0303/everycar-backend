package com.road_friends.everycar.user.mapper;

import com.road_friends.everycar.user.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface APIUserMapper {
    void save(UserDTO userDTO);
    void insertUserRole(@Param("userId") Long userId, @Param("roleId") int roleId);
}
