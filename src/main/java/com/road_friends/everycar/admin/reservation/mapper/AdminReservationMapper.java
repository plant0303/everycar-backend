package com.road_friends.everycar.admin.reservation.mapper;

import com.road_friends.everycar.admin.reservation.dto.ReservationDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminReservationMapper {
    // 페이징 처리된 예약 목록 조회
    List<ReservationDTO> findAll(@Param("offset") int offset, @Param("size") int size);

    // 전체 예약 수 조회
    int countAll();

    // 예약 단건 조회 (수정 폼용)
    ReservationDTO findById(Long reservationId);

    // 예약 상태 및 정보 수정
    int updateReservation(ReservationDTO reservationDTO);
}