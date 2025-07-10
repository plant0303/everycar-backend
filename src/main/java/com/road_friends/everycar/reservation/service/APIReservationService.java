package com.road_friends.everycar.reservation.service;

import com.road_friends.everycar.reservation.dto.CarDTO;
import com.road_friends.everycar.reservation.mapper.APIReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class APIReservationService {
    @Autowired
    private APIReservationMapper APIReservationMapper;

    public Map<String, Object> getAvailableCars(String province, String district, LocalDateTime rentalDatetime, LocalDateTime returnDatetime){
        // DB 검색 결과
        List<CarDTO> availableCars = APIReservationMapper.getAvailableCars(province, district, rentalDatetime, returnDatetime);
        // 차량정보 리스트
        // List<Map<String, Objects>> carList = new ArrayList<>();

        Map<String, Object> carListMap = new HashMap<>();
        carListMap.put("cars", availableCars);
        return carListMap;
    }
}
