package com.road_friends.everycar.payment.controller;

import com.road_friends.everycar.payment.dto.PaymentCheckRequest;
import com.road_friends.everycar.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/complete")
    public ResponseEntity<?> completePayment(@RequestBody PaymentCheckRequest request) {
        try {
            // 1. 결제 검증 및 예약 저장 실행
            boolean isSuccess = paymentService.verifyAndSaveReservation(request);

            if (isSuccess) {
                return ResponseEntity.ok("예약 및 결제가 완료되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제 금액이 일치하지 않습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류: " + e.getMessage());
        }
    }
}