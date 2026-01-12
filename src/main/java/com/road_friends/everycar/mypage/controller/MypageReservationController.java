package com.road_friends.everycar.mypage.controller;

import com.road_friends.everycar.mypage.dto.ReservationDetailDTO;
import com.road_friends.everycar.mypage.dto.ReservationResponseDTO;
import com.road_friends.everycar.mypage.service.MypageReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mypage/reservation")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class MypageReservationController {

    private final MypageReservationService mypageReservationService;

    @GetMapping("/{userNum}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservations(@PathVariable Long userNum) {
        List<ReservationResponseDTO> list = mypageReservationService.getMyReservations(userNum);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/detail/{reservationId}")
    public ResponseEntity<ReservationDetailDTO> getReservationDetail(@PathVariable Long reservationId) {
        ReservationDetailDTO detail = mypageReservationService.getReservationDetail(reservationId);
        if (detail != null) {
            return ResponseEntity.ok(detail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}