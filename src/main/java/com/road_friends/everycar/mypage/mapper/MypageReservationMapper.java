package com.road_friends.everycar.mypage.mapper;

import com.road_friends.everycar.mypage.dto.ReservationDetailDTO;
import com.road_friends.everycar.mypage.dto.ReservationResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MypageReservationMapper {
    // 특정 유저의 예약 내역 전체 조회
    List<ReservationResponseDTO> selectReservationsByUser(@Param("userNum") Long userNum);
    ReservationDetailDTO selectReservationDetail(@Param("reservationId") Long reservationId);
}