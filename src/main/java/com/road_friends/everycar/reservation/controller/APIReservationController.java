package com.road_friends.everycar.reservation.controller;

import com.road_friends.everycar.reservation.dto.CarDTO;
import com.road_friends.everycar.reservation.dto.ParkingDTO;
import com.road_friends.everycar.reservation.service.APIReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/reservation")
@CrossOrigin(origins = "http://localhost:3000")
public class APIReservationController {

    @Autowired
    private APIReservationService APIReservationService;

    // 예약 가능한 차량 리스트 조회
    @GetMapping("/cars")
    public ResponseEntity<?> getAvailableCars(
            @RequestParam("parkingId") Integer parkingId,
            @RequestParam("rentalDatetime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rentalDatetime,
            @RequestParam("returnDatetime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime returnDatetime
    ) {
        try {
            List<CarDTO> availableCars = APIReservationService.getAvailableCars(parkingId, rentalDatetime, returnDatetime);
            return ResponseEntity.ok(availableCars);
        } catch (IllegalArgumentException e) {
            // 대여/반납 시간 유효성 오류
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ConfigDataResourceNotFoundException e) {
            // 대여 가능한 차량이 없는 경우
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // 기타 서버 오류
            return new ResponseEntity<>("차량 조회 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 특정 차량 상세 조회
    @GetMapping("/cars/{carId}")
    public ResponseEntity <Map<String, Object>> getCarById(@PathVariable("carId") int carId,
                                                           @RequestParam("rental_datetime") String rentalDatetimeStr,
                                                           @RequestParam("return_datetime") String returnDatetimeStr) {

        LocalDateTime rentalDatetime = LocalDateTime.parse(rentalDatetimeStr);
        LocalDateTime returnDatetime = LocalDateTime.parse(returnDatetimeStr);
        Map<String, Object> carDetail = APIReservationService.getCarInfo(carId, rentalDatetime, returnDatetime);
        return ResponseEntity.ok(carDetail);
    }

    // 예약 옵션 페이지
    @GetMapping("/reservationCar/{carId}")
    public ResponseEntity reserveCar(@PathVariable("carId") int carId) {
        // 반납 가능 장소 조회
        List<ParkingDTO> parkingList = APIReservationService.getParkingStation(carId);
        return ResponseEntity.ok(parkingList);
    }

    // 결제페이지

    // 예약 생성
}
