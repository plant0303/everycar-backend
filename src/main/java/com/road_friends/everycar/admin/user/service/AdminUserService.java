package com.road_friends.everycar.admin.user.service;

import com.road_friends.everycar.admin.user.mapper.AdminUserMapper;
import com.road_friends.everycar.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final AdminUserMapper adminUserMapper;

    public List<UserDTO> getAllUsers() {
        return adminUserMapper.findAllUsers();
    }

    public UserDTO getUserByNum(Long userNum) {
        return adminUserMapper.findByUserNum(userNum);
    }

    @Transactional
    public void updateUser(UserDTO userDTO) {
        adminUserMapper.updateUser(userDTO);
    }
}
