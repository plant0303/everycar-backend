package com.road_friends.everycar.reservation.controller;

import com.road_friends.everycar.reservation.dto.CarDTO;
import com.road_friends.everycar.reservation.service.FastReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("api/reservation")
public class APIReservationController {

    @Autowired
    private FastReservationService fastReservationService;


    @GetMapping("/cars")
    public ResponseEntity<Map<String, Object>> selectCars(@RequestParam String province,
                                                          @RequestParam String district){

        Map<String, Object> availableCars = fastReservationService.getAvailableCars(province, district);
        return ResponseEntity.ok(availableCars);
    }
}
