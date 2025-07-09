package com.road_friends.everycar.reservation.mapper;

import com.road_friends.everycar.reservation.dto.CarDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FastReservationMapper {
    List<CarDTO> getAvailableCars(@Param("province") String province,
                                  @Param("district") String district);
}
