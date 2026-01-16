package com.road_friends.everycar.admin.user.mapper;

import com.road_friends.everycar.user.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminUserMapper {
    // 회원 전체 목록 조회
    List<UserDTO> findAllUsers();

    // 특정 회원 상세 조회
    UserDTO findByUserNum(Long userNum);

    // 회원 정보 업데이트
    int updateUser(UserDTO userDTO);
}
