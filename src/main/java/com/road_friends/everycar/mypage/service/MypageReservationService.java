package com.road_friends.everycar.mypage.service;

import com.road_friends.everycar.mypage.dto.ReservationDetailDTO;
import com.road_friends.everycar.mypage.dto.ReservationResponseDTO;
import com.road_friends.everycar.mypage.mapper.MypageReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageReservationService {

    private final MypageReservationMapper mypageReservationMapper;

    public List<ReservationResponseDTO> getMyReservations(Long userNum) {
        return mypageReservationMapper.selectReservationsByUser(userNum);
    }

    public ReservationDetailDTO getReservationDetail(Long reservationId) {
        return mypageReservationMapper.selectReservationDetail(reservationId);
    }
}