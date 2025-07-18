package com.road_friends.everycar.user.service;

import com.road_friends.everycar.user.component.JwtUtil;
import com.road_friends.everycar.user.dto.RoleDTO;
import com.road_friends.everycar.user.dto.UserDTO;
import com.road_friends.everycar.user.mapper.APIUserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class APIUserService {

    private final PasswordEncoder passwordEncoder;
    private final APIUserMapper APIUserMapper;
    private final JwtUtil jwtUtil;

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

    public UserDTO getUserById(String userId) {
        return APIUserMapper.findByUsername(userId);
    }

    public Map<String, String> login(String userId, String password) {
        UserDTO user = APIUserMapper.findByUsername(userId);

        if (user == null || !passwordEncoder.matches(password, user.getUserPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        List<String> roles = user.getRoles().stream()
                .map(RoleDTO::getName)
                .collect(Collectors.toList());

        String accessToken = jwtUtil.generateToken(userId, user.getUserNum(), roles);
        String refreshToken = jwtUtil.generateRefreshToken(userId);

        // DB에 리프레시 토큰 저장
        Timestamp refreshTokenExpiredAt = new Timestamp(jwtUtil.getExpirationFromToken(refreshToken).getTime());
        APIUserMapper.updateRefreshToken(user.getUserNum(), refreshToken, refreshTokenExpiredAt);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    public void logout(String userId) {
        UserDTO user = APIUserMapper.findByUsername(userId);
        if (user == null) {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }

        // DB에 저장된 리프레시 토큰 제거
        APIUserMapper.updateRefreshToken(user.getUserNum(), null, null);
    }

}
