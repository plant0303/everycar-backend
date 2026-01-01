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

    @Value("${jwt.expiration-time}") // 액세스 토큰 유효 시간
    private long EXPIRATION_TIME; // ex. 1일 = 1000 * 60 * 60 * 24

    @Value("${jwt.refresh-expiration-time}") // 리프레시 토큰 유효 시간
    private long REFRESH_EXPIRATION_TIME; //

    /**
     * 액세스 토큰 생성
     */
    public String generateToken(String userId, Long userNum, List<String> roles) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("user_num", userNum)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 리프레시 토큰 생성 (userId만 포함, 최소한의 정보)
     */
    public String generateRefreshToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 토큰에서 userId 추출
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * 토큰에서 user_num 추출
     */
    public Long extractUserNum(String token) {
        return extractClaims(token).get("user_num", Long.class);
    }

    /**
     * 토큰에서 roles 추출
     */
    public List<String> extractRoles(String token) {
        return extractClaims(token).get("roles", List.class);
    }

    /**
     * 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = extractClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // 만료일자 추출
    public Date getExpirationFromToken(String token) {
        return extractClaims(token).getExpiration();
    }

    /**
     * Claims 객체 추출
     */
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }



}
