package com.road_friends.everycar.admin.user.mapper;

import com.road_friends.everycar.admin.user.dto.ReservationViewDTO;
import com.road_friends.everycar.user.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminUserMapper {
    // 회원 전체 목록 조회
    List<UserDTO> findAllUsers(@Param("keyword") String keyword,
                               @Param("offset") int offset,
                               @Param("size") int size);

    // 검색 조건에 맞는 전체 데이터 개수 (페이지네이션 계산용)
    int countUsers(@Param("keyword") String keyword);

    // 특정 회원 상세 조회
    UserDTO findByUserNum(Long userNum);

    // 특정 유저의 대여 내역 조회
    List<ReservationViewDTO> findReservationsByUserNum(Long userNum);

    // 회원 정보 업데이트
    int updateUser(UserDTO userDTO);
}
