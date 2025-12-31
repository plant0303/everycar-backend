package com.road_friends.everycar.reservation.mapper;

import com.road_friends.everycar.reservation.dto.APIReservationDTO;
import com.road_friends.everycar.reservation.dto.CarDTO;
import com.road_friends.everycar.reservation.dto.ParkingDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface APIReservationMapper {
    List<CarDTO> getAvailableCars(
            @Param("parkingId") Integer parkingId,
            @Param("rentalDatetime") LocalDateTime rentalDatetime,
            @Param("returnDatetime") LocalDateTime returnDatetime
    );
    CarDTO getCarById(int carId);

    int getAmountHour(int carId);

    int getAmountDay(int carId);

    List<ParkingDTO> getAllParkingStation(int carId);

    APIReservationDTO findContractDetails(
            @Param("carId") int carId,
            @Param("userNum") Long userNum,
            @Param("parkingId") int parkingId
    );
}
