// JwtUtilTest.java
package com.festival.apigateway.common.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        // JwtUtil 인스턴스 초기화
        jwtUtil = new JwtUtil();
    }

    @Test
    void testGenerateAndValidateToken() {
        // 테스트용 사용자 이름
        String username = "testUser";

        // Access 토큰과 Refresh 토큰 생성
        String accessToken = jwtUtil.generateToken(username, "ACCESS");
        String refreshToken = jwtUtil.generateToken(username, "REFRESH");

        // 토큰이 null이 아님을 확인
        assertNotNull(accessToken);
        assertNotNull(refreshToken);

        // 토큰이 유효함을 확인
        assertTrue(jwtUtil.validateToken(accessToken));
        assertTrue(jwtUtil.validateToken(refreshToken));
    }

    @Test
    void testExtractUsername() {
        // 테스트용 사용자 이름
        String username = "testUser";

        // Access 토큰 생성
        String token = jwtUtil.generateToken(username, "ACCESS");

        // 토큰에서 사용자 이름 추출 및 확인
        String extractedUsername = jwtUtil.extractUsername(token);
        assertEquals(username, extractedUsername);
    }
}
