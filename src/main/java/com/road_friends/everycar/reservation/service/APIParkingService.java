package com.road_friends.everycar.reservation.service;
import com.road_friends.everycar.reservation.dto.ParkingDTO;
import com.road_friends.everycar.reservation.mapper.APIParkingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class APIParkingService {

    private final APIParkingMapper parkingMapper;

    @Autowired
    public APIParkingService(APIParkingMapper parkingMapper) {
        this.parkingMapper = parkingMapper;
    }

    public List<ParkingDTO> getParkingListByRegion(String region) {
        return parkingMapper.selectParkingByRegion(region);
    }
}
