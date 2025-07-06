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

    // 일반 회원가입
    public void signup(UserDTO userDTO) {
        String rawPw = userDTO.getUserPassword();
        String encodedPw = passwordEncoder.encode(rawPw);

        System.out.println("암호화 전 비밀번호: " + rawPw);
        System.out.println("암호화 후 비밀번호: " + encodedPw);

        userDTO.setUserPassword(encodedPw);
        userDTO.setEnabled(userDTO.isEnabled());

        // 사용자 등록
        APIUserMapper.save(userDTO);

        // 권한 등록
        APIUserMapper.insertUserRole(userDTO.getUserNum(), 1);
    }
}
