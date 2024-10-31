package com.festival.apigateway.common.service;

import com.festival.apigateway.common.kafka.LoginRequestProducer;
import com.festival.apigateway.common.kafka.LoginResponseConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.festival.apigateway.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private LoginRequestProducer loginRequestProducer;  // Kafka 로그인 요청 Producer

    @Autowired
    private LoginResponseConsumer loginResponseConsumer;  // Kafka 로그인 응답 Consumer

    /**
     * 로그인 요청을 처리하여 Kafka로 이벤트를 발행하고 응답을 처리하는 메서드
     * @param email 사용자의 이메일
     * @param password 사용자의 비밀번호
     * @return JWT 토큰 또는 인증 실패 메시지
     */
    public String handleLoginRequest(String email, String password) {
        try {
            // Kafka로 로그인 요청 메시지 전송
            loginRequestProducer.sendLoginRequest(email, password);

            // 응답을 기다릴 CompletableFuture 생성
            CompletableFuture<String> responseFuture = new CompletableFuture<>();
            loginResponseConsumer.setResponseFuture(email, responseFuture);

            // 타임아웃과 함께 응답 대기
            return responseFuture.get(10, TimeUnit.SECONDS); // 10초 타임아웃 설정

        } catch (TimeoutException e) {
            log.error("Login request timed out for email: {}", email);
            return "Login request timed out. Please try again.";
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error processing login request for email: {}", email, e);
            return "Error processing login request. Please try again.";
        }
    }

}