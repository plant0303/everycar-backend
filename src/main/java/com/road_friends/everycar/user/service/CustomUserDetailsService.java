package com.road_friends.everycar.user.service;

import com.road_friends.everycar.user.component.CustomUserDetails;
import com.road_friends.everycar.user.dto.RoleDTO;
import com.road_friends.everycar.user.dto.UserDTO;
import com.road_friends.everycar.user.mapper.APIUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final APIUserMapper apiUserMapper  ;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserDTO userDTO = apiUserMapper.findByUsername(userId);
        // 예외처리
        if(userDTO == null){
          System.out.println("해당 ID 없음");
          throw new UsernameNotFoundException("해당 유저를 찾을 수 없습니다: " + userId);
        }

        // 권한 생성
        // CustomUserDetails 객체 생성, usernum, id, pw, roles 값 넘겨주기
        List<String> roles = userDTO.getRoles().stream()
                .map(RoleDTO::getName)
                .collect(Collectors.toList());
        return new CustomUserDetails(userDTO.getUserNum(), userDTO.getUserId(), userDTO.getUserPassword(), roles);
    }



}
