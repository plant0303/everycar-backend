package com.road_friends.everycar.admin.user.service;

import com.road_friends.everycar.admin.user.dto.ReservationViewDTO;
import com.road_friends.everycar.admin.user.mapper.AdminUserMapper;
import com.road_friends.everycar.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final AdminUserMapper adminUserMapper;

    @Transactional(readOnly = true)
    public Map<String, Object> getUserList(String keyword, int page) {
        int size = 10; // 페이지당 출력 개수
        int offset = (page - 1) * size;

        List<UserDTO> users = adminUserMapper.findAllUsers(keyword, offset, size);
        int totalCount = adminUserMapper.countUsers(keyword);
        int totalPages = (int) Math.ceil((double) totalCount / size);

        Map<String, Object> result = new HashMap<>();
        result.put("users", users);
        result.put("currentPage", page);
        result.put("totalPages", totalPages);
        result.put("keyword", keyword);

        return result;
    }

    public UserDTO getUserByNum(Long userNum) {
        return adminUserMapper.findByUserNum(userNum);
    }

    public List<ReservationViewDTO> getReservationsByUser(Long userNum) {
        return adminUserMapper.findReservationsByUserNum(userNum);
    }

    @Transactional
    public void updateUser(UserDTO userDTO) {
        adminUserMapper.updateUser(userDTO);
    }
}
