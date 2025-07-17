package com.road_friends.everycar.mypage.service;

import com.road_friends.everycar.mypage.dto.MypageDTO;
import com.road_friends.everycar.mypage.mapper.MypageMapper;
import org.springframework.stereotype.Service;

@Service
public class MypageService {

    private final MypageMapper mypageMapper;

    public MypageService(MypageMapper mypageMapper) {
        this.mypageMapper = mypageMapper;
    }

    public MypageDTO getUserInfo(String userId) {
        return mypageMapper.getUserInfo(userId);
    }
}
