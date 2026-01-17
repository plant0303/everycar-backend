package com.road_friends.everycar.admin.car.service;

import com.road_friends.everycar.admin.car.dto.CarAdminDTO;
import com.road_friends.everycar.admin.car.mapper.AdminCarMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminCarService {
    private final AdminCarMapper carMapper;

    public Map<String, Object> getCarList(int page, String keyword, Integer status) {
        int size = 10;
        int offset = (page - 1) * size;
        List<CarAdminDTO> list = carMapper.findAll(offset, size, keyword, status);
        int totalCount = carMapper.countAll(keyword, status);
        int totalPages = (int) Math.ceil((double) totalCount / size);

        int pageBlock = 5; // 한 번에 보여줄 번호 개수
        int startPage = ((page - 1) / pageBlock) * pageBlock + 1;
        int endPage = Math.min(startPage + pageBlock - 1, totalPages);

        Map<String, Object> res = new HashMap<>();
        res.put("cars", list);
        res.put("totalPages", totalPages);
        res.put("currentPage", page);
        res.put("startPage", startPage);
        res.put("endPage", endPage);

        // 이전/다음 블록 계산
        res.put("hasPrev", startPage > 1);
        res.put("hasNext", endPage < totalPages);
        res.put("prevPage", startPage - 1); // 이전 블록의 마지막 (예: 6->5)
        res.put("nextPage", endPage + 1);   // 다음 블록의 시작 (예: 5->6)

        return res;
    }

    // 차량 등록/수정 시 등급에 따른 옵션 자동 설정
    public void setOptionsByGrade(CarAdminDTO dto) {
        if ("Premium".equals(dto.getCarGrade())) {
            dto.setCarOptions("네비게이션,하이패스,블랙박스,후방카메라,열선시트");
        } else {
            dto.setCarOptions("블랙박스,하이패스,열선시트");
        }
    }

    @Transactional
    public void registerCar(CarAdminDTO dto) {
        setOptionsByGrade(dto);
        carMapper.insertCar(dto);
    }

    public CarAdminDTO getCar(Integer id) { return carMapper.findById(id); }

    public List<Map<String, Object>> getCarReservations(Integer carId) {
        return carMapper.findReservationsByCarId(carId);
    }

    @Transactional
    public void updateCar(CarAdminDTO dto) {
        setOptionsByGrade(dto);
        carMapper.updateCar(dto);
    }

    public void removeCar(Integer id) { carMapper.deleteCar(id); }

    // 주차장 검색 목록 (Select Box용)
    public List<Map<String, Object>> getAllParkingLots() {
        return carMapper.findAllParkingLots();
    }

    // 모델 검색 목록 (Select Box용)
    public List<Map<String, Object>> getAllModels() {
        return carMapper.findAllModels();
    }
}