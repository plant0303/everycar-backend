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
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.get("userId"), user.get("userPassword"))
            );

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.get("userId"));

            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            Long userNum = ((CustomUserDetails) userDetails).getUserNum();

            String token = jwtUtil.generateToken(userDetails.getUsername(), userNum, roles);

            return ResponseEntity.ok(Map.of("token", token));

        } catch (AuthenticationException e) {
            // 인증 실패 시 401 반환
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        } catch (Exception e) {
            // 그 외 에러는 500
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }



}
