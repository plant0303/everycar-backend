package com.road_friends.everycar.user.controller;

import com.road_friends.everycar.user.component.CustomUserDetails;
import com.road_friends.everycar.user.component.JwtUtil;
import com.road_friends.everycar.user.dto.UserDTO;
import com.road_friends.everycar.user.service.APIUserService;
import com.road_friends.everycar.user.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class APIUserController {

    private final APIUserService APIUserService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserDTO userDTO) {
        APIUserService.signup(userDTO);
        return ResponseEntity.ok("Signup successful");
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> user) {
        try {
            String userId = user.get("userId");
            String rawPassword = user.get("userPassword");

            // DB 사용자 정보 조회 (인증 전에 미리 조회)
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);

            String encodedPassword = userDetails.getPassword();
            System.out.println("입력된 비밀번호: " + rawPassword);
            System.out.println("DB에 저장된 암호화된 비밀번호: " + encodedPassword);
            System.out.println("비밀번호 일치 여부: " + passwordEncoder.matches(rawPassword, encodedPassword));

            // 이제 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userId, rawPassword)
            );

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            Long userNum = ((CustomUserDetails) userDetails).getUserNum();
            String token = jwtUtil.generateToken(userDetails.getUsername(), userNum, roles);

            return ResponseEntity.ok(Map.of("token", token));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }
}
