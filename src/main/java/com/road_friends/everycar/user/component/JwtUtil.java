package com.road_friends.everycar.user.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
@Component
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.expiration-time}") // 보안성을 위해 환경변수로 관리 권장
    private long EXPIRATION_TIME; // 1일 (ms)

    // JWT 토큰 생성 (userNum 추가)
    public String generateToken(String userId, Long userNum, List<String> roles) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("user_num", userNum)  // user_num 추가
                .claim("roles", roles) // 권한 정보 추가
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // JWT 토큰에서 사용자 정보 추출
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // user_num 추출
    public Long extractUserNum(String token) {
        return extractClaims(token).get("user_num", Long.class);
    }

    // 권한(roles) 정보 추출
    public List<String> extractRoles(String token) {
        return extractClaims(token).get("roles", List.class);
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        return !extractClaims(token).getExpiration().before(new Date());
    }

    // Claims 추출
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
