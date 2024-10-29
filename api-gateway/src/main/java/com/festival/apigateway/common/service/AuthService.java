package com.festival.apigateway.common.service;

import com.festival.apigateway.common.kafka.LoginRequestProducer;
import com.festival.apigateway.common.kafka.LoginResponseConsumer;
import org.springframework.stereotype.Service;
import com.festival.apigateway.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;

@Service
public class AuthService {

    @Autowired
    private LoginRequestProducer loginRequestProducer;  // Kafka 로그인 요청 Producer

    @Autowired
    private LoginResponseConsumer loginResponseConsumer;  // Kafka 로그인 응답 Consumer

    @Autowired
    private JwtUtil jwtUtil;  // JWT 토큰 생성 및 검증 유틸리티

    /**
     * 로그인 요청을 처리하여 Kafka로 이벤트를 발행하고 응답을 처리하는 메서드
     * @param email 사용자의 이메일
     * @param password 사용자의 비밀번호
     * @return JWT 토큰 또는 인증 실패 메시지
     */
    public String handleLoginRequest(String email, String password) {
        // CompletableFuture로 응답 대기
        CompletableFuture<String> responseFuture = new CompletableFuture<>();

        // Consumer에 CompletableFuture를 설정하여 이메일에 대한 응답 대기
        loginResponseConsumer.setResponseFuture(email, responseFuture);

        // Kafka로 로그인 요청 발행
        loginRequestProducer.sendLoginRequest(email, password);

        try {
            // CompletableFuture를 통해 응답이 완료될 때까지 대기 후 결과 반환
            return responseFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred while processing login request.";
        }
    }

}