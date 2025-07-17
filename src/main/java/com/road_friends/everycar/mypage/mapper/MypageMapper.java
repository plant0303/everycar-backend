package com.road_friends.everycar.mypage.mapper;

import com.road_friends.everycar.mypage.dto.MypageDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MypageMapper {
    MypageDTO getUserInfo(String userId);
}
