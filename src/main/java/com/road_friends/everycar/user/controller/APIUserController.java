package com.road_friends.everycar.user.controller;

import com.road_friends.everycar.user.dto.UserDTO;
import com.road_friends.everycar.user.service.APIUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class APIUserController {

    private final APIUserService APIUserService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserDTO userDTO) {
        APIUserService.signup(userDTO);
        return ResponseEntity.ok("Signup successful");
    }


}
