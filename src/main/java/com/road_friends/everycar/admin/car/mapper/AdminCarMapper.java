package com.road_friends.everycar.admin.car.mapper;

import com.road_friends.everycar.admin.car.dto.CarAdminDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminCarMapper {
    // 차량 목록 조회 (검색 및 필터 포함)
    List<CarAdminDTO> findAll(@Param("offset") int offset, @Param("size") int size,
                              @Param("keyword") String keyword, @Param("status") Integer status);

    // 전체 차량 수 조회 (페이징용)
    int countAll(@Param("keyword") String keyword, @Param("status") Integer status);

    // 차량 단건 조회 (수정 폼용 - 현재 대여 정보 포함)
    CarAdminDTO findById(Integer carId);
    List<Map<String, Object>> findReservationsByCarId(Integer carId);

    // 신규 차량 등록
    void insertCar(CarAdminDTO car);

    // 차량 정보 수정
    void updateCar(CarAdminDTO car);

    // 차량 삭제
    void deleteCar(Integer carId);

    // [추가] 주차장 전체 목록 조회 (Select Box용)
    List<Map<String, Object>> findAllParkingLots();

    // [추가] 모델 전체 목록 조회 (Select Box용)
    List<Map<String, Object>> findAllModels();
}