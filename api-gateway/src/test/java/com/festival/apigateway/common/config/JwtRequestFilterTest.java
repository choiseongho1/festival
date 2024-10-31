package com.festival.apigateway.common.config;

import com.festival.apigateway.common.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class JwtRequestFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);
    }

    @Test
    void testDoFilterInternal_WithValidToken() throws ServletException, IOException {
        // 가짜 유효 토큰 설정
        String token = "validToken";
        request.addHeader("Authorization", "Bearer " + token);

        // jwtUtil이 유효한 토큰을 반환하도록 설정
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.extractUsername(token)).thenReturn("testuser");

        // 필터 실행
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // filterChain의 doFilter가 호출됐는지 확인
        verify(filterChain, times(1)).doFilter(request, response);
        assertEquals(200, response.getStatus(), "필터 체인은 상태 코드 200으로 요청을 통과시켜야 합니다.");
    }

    @Test
    void testDoFilterInternal_WithInvalidToken() throws ServletException, IOException {
        // 가짜 잘못된 토큰 설정
        String token = "invalidToken";
        request.addHeader("Authorization", "Bearer " + token);

        // jwtUtil이 유효하지 않은 토큰을 반환하도록 설정
        when(jwtUtil.validateToken(token)).thenReturn(false);

        // 필터 실행
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // filterChain의 doFilter가 호출됐는지 확인
        verify(filterChain, times(1)).doFilter(request, response);
        assertEquals(200, response.getStatus(), "잘못된 토큰이 있어도 필터 체인은 상태 코드 200으로 요청을 통과시켜야 합니다.");
    }


    @Test
    void testDoFilterInternal_NoTokenProvided() throws ServletException, IOException {
        // Authorization 헤더가 없는 경우

        // 필터 실행
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // filterChain의 doFilter가 호출됐는지 확인
        verify(filterChain, times(1)).doFilter(request, response);
        assertEquals(200, response.getStatus(), "토큰이 없을 경우에도 필터 체인은 상태 코드 200으로 요청을 통과시켜야 합니다.");
    }
}
