package com.road_friends.everycar.mypage.mapper;

import com.road_friends.everycar.mypage.dto.UserDTO;
import com.road_friends.everycar.mypage.dto.UserLicenseDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MypageUserMapper {
    UserDTO findByUserNum(Long userNum);
    int updateUserBasic(UserDTO userDto);

    //    면허
    UserLicenseDTO findLicenseByUserNum(Long userNum); // 조회
    int insertLicense(UserLicenseDTO licenseDto); // 등록
    int updateLicense(UserLicenseDTO licenseDto); // 수정
    int deleteLicense(Long userNum); // 삭제
}
