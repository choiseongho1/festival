package com.festival.apigateway.common.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.util.Date;
import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    // JWT 서명에 사용될 비밀키 (실제 운영환경에서는 외부 설정으로 관리해야 함)
    private final String SECRET_KEY = "VlwEyVBsYt9V7zq57TejMnVUyzblYcfPQye08f7MGVA9XkHa\n";
    private final SecretKey key;

    // 토큰 만료 시간 설정 (ms 단위)
    static final int ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15;    // 15분
    static final int REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7;  // 7일

    public JwtUtil() {
        // HMAC-SHA 알고리즘을 사용하여 비밀키 생성
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * 사용자 정보와 토큰 타입을 기반으로 JWT 토큰 생성
     * @param username 사용자 식별자
     * @param tokenType 토큰 타입 ("ACCESS" 또는 "REFRESH")
     * @return 생성된 JWT 토큰
     */
    public String generateToken(String username, String tokenType) {
        int expirationTime = getExpirationTime(tokenType);
        return Jwts.builder()
                .setSubject(username)        // 토큰 제목 (사용자 식별자)
                .setIssuedAt(new Date())     // 토큰 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))  // 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS256)  // HS256 알고리즘으로 서명
                .compact();
    }

    /**
     * 토큰 타입에 따른 만료 시간 반환
     * @param tokenType 토큰 타입
     * @return 만료 시간 (밀리초)
     */
    private int getExpirationTime(String tokenType) {
        if ("ACCESS".equalsIgnoreCase(tokenType)) {
            return ACCESS_TOKEN_EXPIRATION;
        } else if ("REFRESH".equalsIgnoreCase(tokenType)) {
            return REFRESH_TOKEN_EXPIRATION;
        } else {
            throw new IllegalArgumentException("올바르지 않은 Token Type 입니다.");
        }
    }

    /**
     * 토큰의 유효성 검증
     * @param token 검증할 토큰
     * @return 유효성 여부
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // 토큰이 유효하지 않은 경우 (만료, 변조 등)
            return false;
        }
    }

    /**
     * 토큰에서 사용자 이름 추출
     * @param token JWT 토큰
     * @return 사용자 이름
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 리프레시 토큰 생성
     * @param username 사용자 식별자
     * @return 생성된 리프레시 토큰
     */
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
