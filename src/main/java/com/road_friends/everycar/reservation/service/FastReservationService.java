package com.road_friends.everycar.reservation.service;

import com.road_friends.everycar.reservation.dto.CarDTO;
import com.road_friends.everycar.reservation.mapper.FastReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FastReservationService {
    @Autowired
    private FastReservationMapper fastReservationMapper;

    public Map<String, Object> getAvailableCars(String province, String district, LocalDateTime rentalDatetime, LocalDateTime returnDatetime){
        // DB 검색 결과
        List<CarDTO> availableCars = fastReservationMapper.getAvailableCars(province, district, rentalDatetime, returnDatetime);
        // 차량정보 리스트
        // List<Map<String, Objects>> carList = new ArrayList<>();

        Map<String, Object> carListMap = new HashMap<>();
        carListMap.put("cars", availableCars);
        return carListMap;
    }
}
