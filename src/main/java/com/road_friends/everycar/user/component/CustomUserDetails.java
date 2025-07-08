package com.road_friends.everycar.user.component;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final Long userNum;               // 사용자 고유 ID (userNum)
    private final String userId;              // 사용자 ID (userId)
    private final String userPassword;        // 비밀번호 (userPassword)
    private final List<GrantedAuthority> authorities; // 권한 리스트

    // 생성자 - 사용자 정보 및 역할 정보 초기화
    public CustomUserDetails(Long userNum, String userId, String userPassword, List<String> roles) {
        this.userNum = userNum;
        this.userId = userId;
        this.userPassword = userPassword;
        this.authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    // 사용자 고유 ID (userNum) 반환
    public Long getUserNum() {
        return userNum;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userPassword;  // 비밀번호 반환
    }

    @Override
    public String getUsername() {
        return userId;  // 사용자 ID 반환
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}