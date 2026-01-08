package com.road_friends.everycar.mypage.controller;

import com.road_friends.everycar.mypage.dto.UserDTO;
import com.road_friends.everycar.mypage.dto.UserLicenseDTO;
import com.road_friends.everycar.mypage.service.MypageUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageUserController {
    private final MypageUserService mypageUserService;

    // 회원 정보 및 면허 정보 조회
    @GetMapping("/{userNum}")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable Long userNum) {
        return ResponseEntity.ok(mypageUserService.getUserByNum(userNum));
    }

    // 회원 정보 및 면허 정보 업데이트
    @PutMapping("/update")
    public ResponseEntity<String> updateUserInfo(@RequestBody UserDTO userDto) {
        mypageUserService.updateUser(userDto);
        return ResponseEntity.ok("정보가 성공적으로 수정되었습니다.");
    }

    // 면허 정보 저장 (등록/수정 통합)
    @PostMapping("/save")
    public ResponseEntity<String> saveLicense(@RequestBody UserLicenseDTO licenseDto) {
        mypageUserService.saveOrUpdateLicense(licenseDto);
        return ResponseEntity.ok("면허 정보가 성공적으로 저장되었습니다.");
    }

    // 면허 정보 삭제
    @DeleteMapping("/{userNum}")
    public ResponseEntity<String> deleteLicense(@PathVariable Long userNum) {
        mypageUserService.deleteLicense(userNum);
        return ResponseEntity.ok("면허 정보가 삭제되었습니다.");
    }
}
