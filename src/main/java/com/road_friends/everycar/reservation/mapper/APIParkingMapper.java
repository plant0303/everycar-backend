package com.road_friends.everycar.reservation.mapper;

import com.road_friends.everycar.reservation.dto.ParkingDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface APIParkingMapper {

    List<ParkingDTO> selectParkingByRegion(@Param("region") String region);
}
