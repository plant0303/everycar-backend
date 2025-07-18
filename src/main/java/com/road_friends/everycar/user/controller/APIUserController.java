package com.road_friends.everycar.user.controller;

import com.road_friends.everycar.user.component.CustomUserDetails;
import com.road_friends.everycar.user.component.JwtUtil;
import com.road_friends.everycar.user.dto.RoleDTO;
import com.road_friends.everycar.user.dto.UserDTO;
import com.road_friends.everycar.user.mapper.APIUserMapper;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String userId = request.get("userId");
            String password = request.get("userPassword");

            Map<String, String> tokens = APIUserService.login(userId, password);
            return ResponseEntity.ok(tokens);


        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        String userId = jwtUtil.extractUsername(refreshToken);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token subject");
        }

        UserDTO user = APIUserService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        System.out.println("클라이언트 리프레시 토큰: " + refreshToken);
        System.out.println("DB 저장된 리프레시 토큰: " + user.getRefreshToken());

        if (!refreshToken.trim().equals(user.getRefreshToken().trim())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token mismatch");
        }

        List<String> roles = user.getRoles().stream()
                .map(RoleDTO::getName)
                .collect(Collectors.toList());

        String newAccessToken = jwtUtil.generateToken(user.getUserId(), user.getUserNum(), roles);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
}
