package com.festival.apigateway.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 리프레시 토큰의 유효기간 (7일, 초 단위)
    private static final long REFRESH_TOKEN_VALIDITY_SECONDS = 7 * 24 * 60 * 60;

    /**
     * Redis에 리프레시 토큰을 저장합니다.
     * @param userId 사용자 ID (리프레시 토큰의 키로 사용)
     * @param refreshToken 발급된 리프레시 토큰
     */
    public void saveRefreshToken(String userId, String refreshToken) {
        // userId를 키로 하여 refreshToken을 Redis에 저장하고, 유효 기간 설정
        redisTemplate.opsForValue().set("refreshToken:" + userId, refreshToken, REFRESH_TOKEN_VALIDITY_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * Redis에서 리프레시 토큰을 가져옵니다.
     * @param userId 사용자 ID
     * @return 저장된 리프레시 토큰
     */
    public String getRefreshToken(String userId) {
        // Redis에서 userId를 키로 한 리프레시 토큰 조회
        return redisTemplate.opsForValue().get("refreshToken:" + userId);
    }

    /**
     * Redis에서 리프레시 토큰을 삭제합니다.
     * @param userId 사용자 ID
     */
    public void deleteRefreshToken(String userId) {
        // userId 키를 삭제하여 해당 리프레시 토큰을 제거
        redisTemplate.delete("refreshToken:" + userId);
    }

    /**
     * 클라이언트에서 받은 리프레시 토큰이 저장된 토큰과 일치하는지 검증합니다.
     * @param userId 사용자 ID
     * @param refreshToken 클라이언트에서 제공된 리프레시 토큰
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateRefreshToken(String userId, String refreshToken) {
        // 저장된 토큰을 조회하고, 입력된 토큰과 비교하여 유효성 확인
        String storedToken = getRefreshToken(userId);
        return refreshToken.equals(storedToken);
    }
}
