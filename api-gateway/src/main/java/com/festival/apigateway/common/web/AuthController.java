package com.festival.apigateway.common.web;

import com.festival.apigateway.common.service.AuthService;
import com.festival.apigateway.common.service.RefreshTokenService;
import com.festival.apigateway.common.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 인증 관련 요청을 처리하는 컨트롤러
 * 로그인, 토큰 재발급 등의 인증 관련 엔드포인트를 제공합니다.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor  // 생성자 주입을 위한 lombok 어노테이션
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    /**
     * 클라이언트의 로그인 요청을 처리하는 엔드포인트
     * 이메일과 비밀번호를 검증하고 JWT 토큰을 발급합니다.
     * 
     * @param email 사용자의 이메일 주소
     * @param password 사용자의 비밀번호
     * @return ResponseEntity<String> 로그인 결과 및 JWT 토큰
     * 
     * 가능한 응답:
     * - 200 OK: 로그인 성공 및 JWT 토큰 발급
     * - 401 Unauthorized: 잘못된 인증 정보
     * - 400 Bad Request: 필수 파라미터 누락
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        String responseMessage = authService.handleLoginRequest(email, password);
        return ResponseEntity.ok(responseMessage);
    }

    /**
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급하는 엔드포인트
     * 
     * @param refreshToken Authorization 헤더에 포함된 리프레시 토큰
     * @return ResponseEntity<?> 새로운 액세스 토큰 또는 에러 메시지
     * 
     * 가능한 응답:
     * - 200 OK: 새로운 액세스 토큰 발급 성공
     * - 401 Unauthorized: 유효하지 않은 리프레시 토큰
     * - 500 Internal Server Error: 서버 내부 오류
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        try {
            // 리프레시 토큰 검증
            if (!jwtUtil.validateToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid refresh token");
            }
            
            // 토큰에서 사용자 정보 추출
            String username = jwtUtil.extractUsername(refreshToken);
            String savedToken = refreshTokenService.getRefreshToken(username);
            
            // 저장된 토큰과 비교
            if (savedToken == null || !savedToken.equals(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Refresh token not found");
            }
            
            // 새로운 액세스 토큰 발급
            String newAccessToken = jwtUtil.generateToken(username, "ACCESS");
            
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);
            
            return ResponseEntity.ok(tokens);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error refreshing token");
        }
    }
}