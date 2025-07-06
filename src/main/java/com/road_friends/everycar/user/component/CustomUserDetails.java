package com.road_friends.everycar.user.component;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {
    // 로그인 성공 후 사용자 정보를 보관
    private final Long userNum;
    private final String userId;
    private final String userPassword;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(Long userNum, String userId, String userPassword, List<String> roles) {
        this.userNum = userNum;
        this.userId = userId;
        this.userPassword = userPassword;
        this.authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public Long getUserNum() {
        return userNum;
    }

    @Override
    public String getUsername(){
        return userId;
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
