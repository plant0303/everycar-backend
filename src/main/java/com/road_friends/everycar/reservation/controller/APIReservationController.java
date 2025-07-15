package com.road_friends.everycar.reservation.controller;

import com.road_friends.everycar.reservation.dto.ParkingDTO;
import com.road_friends.everycar.reservation.service.APIReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/reservation")
public class APIReservationController {

    @Autowired
    private APIReservationService APIReservationService;

    // 예약 가능한 차량 리스트 조회
    @GetMapping("/cars")
    public ResponseEntity<Map<String, Object>> selectCars(@RequestParam String province,
                                                          @RequestParam String district,
                                                          @RequestParam String rental_datetime,
                                                          @RequestParam String return_datetime){

        LocalDateTime rentalDatetime = LocalDateTime.parse(rental_datetime);
        LocalDateTime returnDatetime = LocalDateTime.parse(return_datetime);

        Map<String, Object> availableCars = APIReservationService.getAvailableCars(province, district, rentalDatetime, returnDatetime);
        return ResponseEntity.ok(availableCars);
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
