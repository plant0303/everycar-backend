package com.road_friends.everycar.payment.service;

import com.road_friends.everycar.payment.dto.PaymentCheckRequest;
import com.road_friends.everycar.payment.dto.PortOneTokenResponse;
import com.road_friends.everycar.payment.mapper.ReservationMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ReservationMapper reservationMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${portone.api.key}")
    private String apiKey;

    @Value("${portone.api.secret}")
    private String apiSecret;

    public boolean verifyAndSaveReservation(PaymentCheckRequest request) throws Exception {
        // 1. 포트원 Access Token 발급
        String accessToken = getPortOneToken();

        // 2. 포트원 결제 단건 조회 API 호출 (V2)
        String url = "https://api.portone.io/payments/" + request.getPaymentId();

        HttpHeaders headers = new HttpHeaders();
        // setBearerAuth 대신 직접 문자열로 Authorization 헤더 설정
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON); // JSON 응답을 위해 추가 권장

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        Map<String, Object> paymentData = response.getBody();

        // 3. 금액 검증 (포트원에서 응답온 금액 vs 프론트에서 보낸 금액)
        int paidAmount = (int) ((Map) paymentData.get("amount")).get("total");

        if (paidAmount == request.getTotalPrice()) {
            // 4. 금액이 일치하면 DB에 예약 정보 저장
            reservationMapper.insertReservation(request);
            return true;
        }

        return false;
    }

    private String getPortOneToken() {
        String url = "https://api.portone.io/login/api-secret";

        // 1. 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 2. 바디 구성 (Map 사용)
        Map<String, String> body = new HashMap<>();
        body.put("apiSecret", apiSecret);

        // 3. 요청 객체 생성
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            // 4. postForObject 대신 exchange 권장 (로그 확인이 더 쉬움)
            ResponseEntity<PortOneTokenResponse> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, PortOneTokenResponse.class
            );
            return response.getBody().getAccessToken();
        } catch (Exception e) {
            System.err.println("토큰 발급 실패 상세 로그: " + e.getMessage());
            throw e;
        }
    }
}

