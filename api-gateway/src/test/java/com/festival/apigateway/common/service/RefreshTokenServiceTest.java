package com.festival.apigateway.common.service;

import com.festival.apigateway.common.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class RefreshTokenServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    private final String userId = "testUser";
    private final String refreshToken = "testRefreshToken";

    @BeforeEach
    void setUp() {
        // 각 테스트 실행 전, RedisTemplate의 opsForValue()가 valueOperations를 반환하도록 설정
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testSaveRefreshToken() {
        // 리프레시 토큰을 Redis에 저장하는 기능을 테스트
        refreshTokenService.saveRefreshToken(userId, refreshToken);
        // Redis에 저장되었는지 검증 (set 메소드가 호출되었는지 확인)
        verify(valueOperations, times(1)).set(anyString(), eq(refreshToken), anyLong(), eq(TimeUnit.SECONDS));
    }

    @Test
    void testGetRefreshToken() {
        // Redis에서 리프레시 토큰을 조회하는 기능을 테스트
        when(valueOperations.get(anyString())).thenReturn(refreshToken);
        String token = refreshTokenService.getRefreshToken(userId);
        // 조회된 토큰이 예상한 값과 일치하는지 확인
        assertEquals(refreshToken, token);
    }

    @Test
    void testDeleteRefreshToken() {
        // Redis에서 리프레시 토큰을 삭제하는 기능을 테스트
        refreshTokenService.deleteRefreshToken(userId);
        // Redis에서 삭제되었는지 검증 (delete 메소드가 호출되었는지 확인)
        verify(redisTemplate, times(1)).delete(anyString());
    }

    @Test
    void testValidateRefreshToken_Valid() {
        // 유효한 리프레시 토큰 검증을 테스트
        when(jwtUtil.validateToken(refreshToken)).thenReturn(true);
        when(valueOperations.get(anyString())).thenReturn(refreshToken);

        boolean isValid = refreshTokenService.validateRefreshToken(userId, refreshToken);
        // 토큰이 유효한 경우 true를 반환하는지 확인
        assertTrue(isValid);
    }

    @Test
    void testValidateRefreshToken_Invalid() {
        // 유효하지 않은 리프레시 토큰 검증을 테스트
        when(jwtUtil.validateToken(refreshToken)).thenReturn(false);

        boolean isValid = refreshTokenService.validateRefreshToken(userId, refreshToken);
        // 토큰이 유효하지 않은 경우 false를 반환하는지 확인
        assertFalse(isValid);
    }

    @Test
    void testGetExpirationTime() {
        // 리프레시 토큰의 남은 유효기간을 조회하는 기능을 테스트
        when(redisTemplate.getExpire(anyString(), eq(TimeUnit.SECONDS))).thenReturn(3600L);
        long expirationTime = refreshTokenService.getExpirationTime(userId);
        // 조회된 유효기간이 예상한 값과 일치하는지 확인
        assertEquals(3600L, expirationTime);
    }
} 