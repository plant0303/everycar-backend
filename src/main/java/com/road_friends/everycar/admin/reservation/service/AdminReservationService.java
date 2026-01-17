package com.road_friends.everycar.admin.reservation.service;

import com.road_friends.everycar.admin.reservation.dto.ReservationDTO;
import com.road_friends.everycar.admin.reservation.mapper.AdminReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminReservationService {
    private final AdminReservationMapper reservationMapper;

    public Map<String, Object> getReservationList(int page, String keyword, String filterStatus) {
        int size = 10;
        int offset = (page - 1) * size;

        List<ReservationDTO> list = reservationMapper.findAll(offset, size, keyword, filterStatus);
        int totalCount = reservationMapper.countAll(keyword, filterStatus);
        int totalPages = (int) Math.ceil((double) totalCount / size);

        Map<String, Object> res = new HashMap<>();
        res.put("list", list);
        res.put("currentPage", page);
        res.put("totalPages", totalPages);
        res.put("keyword", keyword);
        res.put("filterStatus", filterStatus); // 뷰에서 상태를 유지하기 위해 추가
        return res;
    }

    public ReservationDTO getReservation(Long id) { return reservationMapper.findById(id); }

    @Transactional
    public void update(ReservationDTO dto) { reservationMapper.updateReservation(dto); }
}