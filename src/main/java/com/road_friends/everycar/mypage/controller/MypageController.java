package com.road_friends.everycar.mypage.controller;

import com.road_friends.everycar.mypage.dto.MypageDTO;
import com.road_friends.everycar.mypage.service.MypageService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class MypageController {

    private final MypageService mypageService;

    public MypageController(MypageService mypageService) {
        this.mypageService = mypageService;
    }

    @GetMapping("/mypage")
    public MypageDTO getUserInfo(){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return mypageService.getUserInfo(userId);
    }
}


