package com.road_friends.everycar.user.service;

import com.road_friends.everycar.user.dto.UserDTO;
import com.road_friends.everycar.user.mapper.APIUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class APIUserService {

    private final PasswordEncoder passwordEncoder;
    private final APIUserMapper APIUserMapper;

    //일반회원가입
    public void signup(UserDTO userDTO) {
        String encodedPw = passwordEncoder.encode(userDTO.getUserPassword());
        userDTO.setUserPassword(encodedPw);
        userDTO.setEnabled(userDTO.isEnabled());

        // 사용자 등록
        APIUserMapper.save(userDTO);
        // 권한 등록
        APIUserMapper.insertUserRole(userDTO.getUserNum(), 1);
    }
}
