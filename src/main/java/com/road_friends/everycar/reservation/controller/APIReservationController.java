package com.road_friends.everycar.reservation.controller;

import com.road_friends.everycar.reservation.service.APIReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("api/reservation")
public class APIReservationController {

    @Autowired
    private APIReservationService APIReservationService;


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
}
