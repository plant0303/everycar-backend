package com.road_friends.everycar.reservation.service;

import com.road_friends.everycar.reservation.dto.APIReservationDTO;
import com.road_friends.everycar.reservation.dto.CarDTO;
import com.road_friends.everycar.reservation.dto.ParkingDTO;
import com.road_friends.everycar.reservation.mapper.APIReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class APIReservationService {
    @Autowired
    private APIReservationMapper APIReservationMapper;

    @Transactional(readOnly = true)
    public List<CarDTO> getAvailableCars(Integer parkingId, LocalDateTime rentalDatetime, LocalDateTime returnDatetime) {
        // 1. 기간 유효성 검사
        validateRentalPeriod(rentalDatetime, returnDatetime);

        // 2. Mapper를 통해 대여 가능한 차량 조회
        List<CarDTO> availableCars = APIReservationMapper.getAvailableCars(parkingId, rentalDatetime, returnDatetime);

        // 3. 각 차량에 대해 총 가격 계산 및 설정
        for (CarDTO car : availableCars) {
            // 모델 정보가 없는 경우를 대비한 안전 코드
            if (car.getModel() != null) {
                Long totalPrice = getTotalPrice(car, rentalDatetime, returnDatetime);
                car.setTotalPrice(totalPrice);
            } else {
            }
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

    public Long getTotalPrice(CarDTO carDTO, LocalDateTime rentalDatetime, LocalDateTime returnDatetime) {
        validateRentalPeriod(rentalDatetime, returnDatetime);

        long totalHours = ChronoUnit.HOURS.between(rentalDatetime, returnDatetime);
        int hourPrice = carDTO.getModel().getModelAmountHour();
        int dayPrice = carDTO.getModel().getModelAmountDay();

        long totalPrice;

        // 24시간 단위는 일 요금으로, 나머지는 시간 요금으로 합산
        long days = totalHours / 24;
        long remainingHours = totalHours % 24;

        if (totalHours < 24) {
            totalPrice = totalHours * hourPrice;
        } else {
            totalPrice = (days * dayPrice) + (remainingHours * hourPrice);
        }

        // 등급 할증 (Primium -> Premium 오타 주의 및 로그 확인)
        if (carDTO.getCarGrade() != null &&
                (carDTO.getCarGrade().equalsIgnoreCase("Premium") || carDTO.getCarGrade().equalsIgnoreCase("Primium"))) {
            totalPrice = (long) (totalPrice * 1.2);
        }

        return totalPrice;
    }

    public Map<String, Object> getCarInfo(int carId, LocalDateTime startTime, LocalDateTime endTime) {
        // 1. Join 쿼리 한 번으로 모든 정보(가격 포함)를 가져옵니다.
        CarDTO car = APIReservationMapper.getCarById(carId);

        // 3. 공통된 계산 로직 호출
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

    // 결제페이지
    public APIReservationDTO getContractDetails(int carId, Long userNum, int parkingId,  LocalDateTime startTime, LocalDateTime endTime) {
        APIReservationDTO dto = APIReservationMapper.findContractDetails(carId, userNum, parkingId);

        // 예외처리
        if (dto == null) {
            throw new RuntimeException("해당 조건(carId: " + carId + ", userNum: " + userNum + ")에 일치하는 예약 정보를 찾을 수 없습니다.");
        }

        Long price = getTotalPrice(dto.getCarDto(), startTime, endTime);
        dto.getCarDto().setTotalPrice(price);
        dto.setTotalPrice(price);

        return dto;
    }
}
