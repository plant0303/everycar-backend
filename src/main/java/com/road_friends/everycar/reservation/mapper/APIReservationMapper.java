package com.road_friends.everycar.reservation.mapper;

import com.road_friends.everycar.reservation.dto.CarDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface APIReservationMapper {
    List<CarDTO> getAvailableCars(@Param("province") String province,
                                  @Param("district") String district,
                                  @Param("rentalDatetime")LocalDateTime rentalDatetime,
                                  @Param("returnDatetime")LocalDateTime returnDatetime);

    CarDTO getCarById(int carId);

    int getAmountHour(int carId);

    int getAmountDay(int carId);
}
