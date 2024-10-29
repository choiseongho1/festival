package com.festival.apigateway.common.web;

import com.festival.apigateway.common.kafka.LoginRequestProducer;
import com.festival.apigateway.common.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;  // AuthService 주입

    /**
     * 클라이언트의 로그인 요청을 처리하는 엔드포인트
     * @param email 사용자의 이메일
     * @param password 사용자의 비밀번호
     * @return 로그인 결과 메시지 또는 JWT 토큰
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        String responseMessage = authService.handleLoginRequest(email, password);
        return ResponseEntity.ok(responseMessage);
    }
}