package com.festival.apigateway.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.festival.apigateway.common.util.JwtUtil;

import java.util.concurrent.TimeUnit;

/**
 * 리프레시 토큰 관리를 위한 서비스 클래스
 * Redis를 사용하여 리프레시 토큰의 저장, 조회, 삭제 및 검증을 처리합니다.
 */
@Service
@RequiredArgsConstructor  // 생성자 주입을 위한 lombok 어노테이션
public class RefreshTokenService {
    
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    // 리프레시 토큰의 유효기간 (14일, 초 단위)
    private static final long REFRESH_TOKEN_VALIDITY_SECONDS = 14 * 24 * 60 * 60;
    // Redis 키 접두사
    private static final String TOKEN_PREFIX = "refreshToken:";

    /**
     * Redis에 리프레시 토큰을 저장합니다.
     * 토큰은 'refreshToken:{userId}' 형식의 키로 저장되며, 설정된 유효기간 후 자동 삭제됩니다.
     * 
     * @param userId 사용자 ID (리프레시 토큰의 키로 사용)
     * @param refreshToken 발급된 리프레시 토큰
     */
    public void saveRefreshToken(String userId, String refreshToken) {
        String key = TOKEN_PREFIX + userId;
        redisTemplate.opsForValue()
            .set(key, refreshToken, REFRESH_TOKEN_VALIDITY_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * Redis에서 리프레시 토큰을 조회합니다.
     * 
     * @param userId 사용자 ID
     * @return 저장된 리프레시 토큰, 없을 경우 null 반환
     */
    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get(TOKEN_PREFIX + userId);
    }

    /**
     * Redis에서 리프레시 토큰을 삭제합니다.
     * 주로 로그아웃이나 토큰 무효화 시 사용됩니다.
     * 
     * @param userId 사용자 ID
     */
    public void deleteRefreshToken(String userId) {
        redisTemplate.delete(TOKEN_PREFIX + userId);
    }

    /**
     * 클라이언트에서 받은 리프레시 토큰의 유효성을 검증합니다.
     * 1. JWT 토큰 자체의 유효성 검증
     * 2. Redis에 저장된 토큰과 일치 여부 확인
     * 
     * @param userId 사용자 ID
     * @param refreshToken 클라이언트에서 제공된 리프레시 토큰
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateRefreshToken(String userId, String refreshToken) {
        // JWT 토큰 유효성 검증
        if (!jwtUtil.validateToken(refreshToken)) {
            return false;
        }
        
        // Redis에 저장된 토큰과 비교
        String storedToken = getRefreshToken(userId);
        return storedToken != null && storedToken.equals(refreshToken);
    }

    /**
     * 리프레시 토큰의 남은 유효기간을 확인합니다.
     * 
     * @param userId 사용자 ID
     * @return 남은 유효기간(초), 토큰이 없으면 -1 반환
     */
    public long getExpirationTime(String userId) {
        return redisTemplate.getExpire(TOKEN_PREFIX + userId, TimeUnit.SECONDS);
    }
}
