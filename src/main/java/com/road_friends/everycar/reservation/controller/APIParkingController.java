package com.road_friends.everycar.reservation.controller;

import com.road_friends.everycar.reservation.dto.ParkingDTO;
import com.road_friends.everycar.reservation.service.APIParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking")
// React 개발 환경(localhost:3000 등)에서의 접근 허용
@CrossOrigin(origins = "http://localhost:3000")
public class APIParkingController {

    private final APIParkingService parkingService;

    @Autowired
    public APIParkingController(APIParkingService parkingService) {
        this.parkingService = parkingService;
    }

    /**
     * 주차장 지역 검색 API
     * 요청 예시: GET /api/parking?region=강남
     */
    @GetMapping
    public ResponseEntity<List<ParkingDTO>> getParkingList(
            @RequestParam(name = "region", required = false, defaultValue = "") String region
    ) {
        List<ParkingDTO> parkingList = parkingService.getParkingListByRegion(region);

        if (parkingList.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        return ResponseEntity.ok(parkingList); // 200 OK
    }
}
