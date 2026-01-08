package com.road_friends.everycar.mypage.service;

import com.road_friends.everycar.mypage.dto.UserDTO;
import com.road_friends.everycar.mypage.dto.UserLicenseDTO;
import com.road_friends.everycar.mypage.mapper.MypageUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class  MypageUserService {
    private final MypageUserMapper mypageUserMapper;

    public UserDTO getUserByNum(Long userNum) {
        return mypageUserMapper.findByUserNum(userNum);
    }

    @Transactional
    public void updateUser(UserDTO userDto) {
        // 1. 기본 유저 정보 업데이트
        mypageUserMapper.updateUserBasic(userDto);
    }

    @Transactional
    public void saveOrUpdateLicense(UserLicenseDTO licenseDto) {
        // 기존 정보 존재 여부 확인
        UserLicenseDTO existing = mypageUserMapper.findLicenseByUserNum(licenseDto.getUserNum());

        if (existing == null) {
            mypageUserMapper.insertLicense(licenseDto);
        } else {
            mypageUserMapper.updateLicense(licenseDto);
        }
    }

    @Transactional
    public void deleteLicense(Long userNum) {
        mypageUserMapper.deleteLicense(userNum);
    }
}
