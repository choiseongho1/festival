package com.festival.apigateway.common.config;

import com.festival.apigateway.common.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            // 유효한 토큰인지 확인
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                if (username != null) {
                    // TODO: 인증 객체 설정 로직 (추가 설정 필요 시)
                }
            }
        }

        // 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }
}
