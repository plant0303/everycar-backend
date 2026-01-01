package com.road_friends.everycar.user.mapper;

import com.road_friends.everycar.user.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;

@Mapper
public interface APIUserMapper {
    void save(UserDTO userDTO);
    void insertUserRole(@Param("userId") Long userId, @Param("roleId") int roleId);
    UserDTO findByUsername(@Param("userId") String userId);
    void updateRefreshToken(
            @Param("userNum") Long userNum,
            @Param("refreshToken") String refreshToken,
            @Param("refreshTokenExpiredAt") Timestamp refreshTokenExpiredAt
    );
}
