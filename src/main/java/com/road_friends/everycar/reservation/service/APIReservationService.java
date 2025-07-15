package com.road_friends.everycar.reservation.service;

import com.road_friends.everycar.reservation.dto.CarDTO;
import com.road_friends.everycar.reservation.mapper.APIReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class APIReservationService {
    @Autowired
    private APIReservationMapper APIReservationMapper;

    public Map<String, Object> getAvailableCars(String province, String district, LocalDateTime rentalDatetime, LocalDateTime returnDatetime){
        List<CarDTO> availableCars = APIReservationMapper.getAvailableCars(province, district, rentalDatetime, returnDatetime);

        for (CarDTO car : availableCars) {
            Long price = getTotalPrice(car, rentalDatetime, returnDatetime);
            car.setTotalPrice(price);  // totalPrice를 DTO에 세팅
        }

        Map<String, Object> carListMap = new HashMap<>();
        carListMap.put("cars", availableCars);  // 이제 리스트는 CarDTO 자체
        return carListMap;
    }


    public Map<String, Object> getCarInfo(int carId, LocalDateTime startTime, LocalDateTime endTime) {
        CarDTO car = APIReservationMapper.getCarById(carId);
        car.getModel().setModelAmountDay(APIReservationMapper.getAmountDay(car.getModel().getModelId()));
        car.getModel().setModelAmountHour(APIReservationMapper.getAmountHour(car.getModel().getModelId()));

        Long price = getTotalPrice(car, startTime, endTime);

        car.setTotalPrice(price);
        Map<String, Object> carDetail = new HashMap<>();
        carDetail.put("car", car);
        return carDetail;
    }

    // 가격 계산 로직
    public Long getTotalPrice(CarDTO carDTO, LocalDateTime rentalDatetime, LocalDateTime returnDatetime){
        Long totalPrice = 0L;
        validateRentalPeriod(rentalDatetime, returnDatetime);

        Long houreBetween = ChronoUnit.HOURS.between(rentalDatetime, returnDatetime); // 시간 차이 계산
        Long dayBetween = ChronoUnit.DAYS.between(rentalDatetime, returnDatetime); // 날짜 차이 계산

        // 최소 4시간, 최대 14일 예약 예외처리
        if(houreBetween < 4){
            throw new IllegalArgumentException("예약은 최소 4시간부터 가능합니다");
        }
        if(dayBetween > 14){
            throw new IllegalArgumentException("예약은 최대 14일까지 가능합니다");
        }

        // 예약 시간이 24시간이 넘어가면 일수로 가격 계산, 24시간 이하일 경우 시간단위 계산
        int hourPrice = carDTO.getModel().getModelAmountHour();
        int dayPrice = carDTO.getModel().getModelAmountDay();
        if(houreBetween < 24) {
            totalPrice = houreBetween * hourPrice;
        } else {
            totalPrice = dayPrice * dayBetween;
        }

        // 차량 등급에 따른 추가금 계산
        if(carDTO.getCarGrade().equalsIgnoreCase("Primium")){
            totalPrice = (long)(totalPrice * 1.2);
        }

        // 가격 로직 예외 처리 할 것
        return totalPrice;
    }

    // 예약 시간 검증
    private void validateRentalPeriod(LocalDateTime start, LocalDateTime end) {
        long hours = ChronoUnit.HOURS.between(start, end);
        long days = ChronoUnit.DAYS.between(start, end);

        if (hours < 4) {
            throw new IllegalArgumentException("예약은 최소 4시간부터 가능합니다.");
        }
        if (days > 14) {
            throw new IllegalArgumentException("예약은 최대 14일까지 가능합니다.");
        }
    }

    public CarDTO getCarById(int carId) {
        return APIReservationMapper.getCarById(carId);
    }

}
