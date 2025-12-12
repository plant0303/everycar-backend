package com.road_friends.everycar.reservation.service;

import com.road_friends.everycar.reservation.dto.CarDTO;
import com.road_friends.everycar.reservation.dto.ParkingDTO;
import com.road_friends.everycar.reservation.mapper.APIReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class APIReservationService {
    @Autowired
    private APIReservationMapper APIReservationMapper;

    @Transactional(readOnly = true)
    public List<CarDTO> getAvailableCars(Integer parkingId, LocalDateTime rentalDatetime, LocalDateTime returnDatetime) {
        // 기간 유효성 검사
        validateRentalPeriod(rentalDatetime, returnDatetime);

        // Mapper를 통해 대여 가능한 차량 조회
        List<CarDTO> availableCars = APIReservationMapper.getAvailableCars(parkingId, rentalDatetime, returnDatetime);

        // 각 차량에 대해 총 가격 계산 및 설정
        for (CarDTO car : availableCars) {
            Long totalPrice = getTotalPrice(car, rentalDatetime, returnDatetime);
            car.setTotalPrice(totalPrice);
        }

        return availableCars;
    }

    private void validateRentalPeriod(LocalDateTime rentalDatetime, LocalDateTime returnDatetime) {
        if (rentalDatetime.isAfter(returnDatetime) || rentalDatetime.isEqual(returnDatetime)) {
            throw new IllegalArgumentException("대여 시작 시간은 반납 시간보다 빨라야 합니다.");
        }

        Long houreBetween = ChronoUnit.HOURS.between(rentalDatetime, returnDatetime);
        Long dayBetween = ChronoUnit.DAYS.between(rentalDatetime, returnDatetime);

        // 최소 4시간 예약 예외처리
        if(houreBetween < 4){
            throw new IllegalArgumentException("예약은 최소 4시간부터 가능합니다.");
        }

        // 최대 14일 예약 예외처리
        // dayBetween이 14일을 초과하는지 확인 (2주 + @일 경우를 대비하여 14일 초과로 체크)
        if(dayBetween > 14 || (dayBetween == 14 && houreBetween > ChronoUnit.HOURS.between(rentalDatetime.toLocalDate().atStartOfDay(), rentalDatetime.toLocalDate().atStartOfDay().plusDays(14)))) {
            if (houreBetween > 14 * 24) { // 336시간 초과
                throw new IllegalArgumentException("예약은 최대 14일까지 가능합니다.");
            }
        }
    }

    public Long getTotalPrice(CarDTO carDTO, LocalDateTime rentalDatetime, LocalDateTime returnDatetime){

        // 1. 기간 유효성 검사 (getAvailableCars에서 이미 호출되었지만, 단독 호출될 경우를 대비하여 포함)
        validateRentalPeriod(rentalDatetime, returnDatetime);

        Long houreBetween = ChronoUnit.HOURS.between(rentalDatetime, returnDatetime);
        Long dayBetween = ChronoUnit.DAYS.between(rentalDatetime, returnDatetime);
        Long totalPrice;

        // 2. 기본 가격 계산
        int hourPrice = carDTO.getModel().getModelAmountHour();
        int dayPrice = carDTO.getModel().getModelAmountDay();

        if (carDTO.getModel() == null) {
            throw new IllegalStateException("모델 정보가 누락되어 가격 계산이 불가능합니다.");
        }

        // 24시간이 넘어가면 일수 (Day)로 가격 계산, 24시간 이하일 경우 시간 단위 계산
        if(houreBetween < 24) {
            // 시간 단위 계산 (1~23시간)
            totalPrice = houreBetween * hourPrice;
        } else {
            // 일 단위 계산 (24시간 이상)
            // 주의: ChronoUnit.DAYS.between()은 시각 정보를 무시하고 날짜 차이만 계산하므로,
            // 실제 24시간 단위의 렌탈 로직과는 다소 다를 수 있습니다. (예: 1일 1시간 = 1일로 계산됨)
            // 기존 로직에 충실하게 dayBetween을 사용합니다.

            // 더 정확한 일수 계산 로직 예시 (기존 로직과 다름):
            // long fullDays = houreBetween / 24;
            // long remainingHours = houreBetween % 24;
            // totalPrice = (fullDays * dayPrice) + (remainingHours * hourPrice);

            // 기존 로직 (단순 일수 차이 계산)에 충실:
            totalPrice = dayPrice * dayBetween;
        }

        // 3. 차량 등급에 따른 추가금 계산 (Primium 등급은 20% 할증)
        if(carDTO.getCarGrade() != null && carDTO.getCarGrade().equalsIgnoreCase("Primium")){
            totalPrice = (long)(totalPrice * 1.2);
        }

        // 4. 가격 로직 예외 처리
        if (totalPrice <= 0) {
            throw new IllegalStateException("계산된 총 가격이 0원 이하입니다. 가격 계산 로직에 오류가 있습니다.");
        }

        return totalPrice;
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

    public CarDTO getCarById(int carId) {
        return APIReservationMapper.getCarById(carId);
    }

    // 반납 가능 장소 조회
    public List<ParkingDTO> getParkingStation(int carId) {
        List<ParkingDTO> parkingList;

        parkingList = APIReservationMapper.getAllParkingStation(carId);
        return parkingList;
    }


}
