package com.festival.apigateway.common.kafka;

import com.festival.apigateway.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class LoginResponseConsumer {

    @Autowired
    private JwtUtil jwtUtil;

    // 이메일별 CompletableFuture를 저장하는 ConcurrentHashMap
    private final Map<String, CompletableFuture<String>> responseFutures = new ConcurrentHashMap<>();

    /**
     * 특정 이메일에 대한 CompletableFuture 설정 메서드
     * @param email 로그인 요청 이메일
     * @param responseFuture 해당 이메일의 응답을 기다리는 CompletableFuture
     */
    public void setResponseFuture(String email, CompletableFuture<String> responseFuture) {
        responseFutures.put(email, responseFuture);
    }

    /**
     * login-responses 토픽을 구독하여 인증 결과를 수신하고 해당 이메일에 맞는 Future에 결과 설정
     * @param message 수신된 Kafka 메시지 (인증 결과 및 이메일 포함)
     */
    @KafkaListener(topics = "login-responses", groupId = "api-gateway-group")
    public void processLoginResponse(String message) {
        String status = extractStatusFromMessage(message);
        String email = extractEmailFromMessage(message);

        String responseMessage;
        if ("success".equals(status)) {
            String accessToken = jwtUtil.generateToken(email, "ACCESS");
            String refreshToken = jwtUtil.generateToken(email, "REFRESH");
            responseMessage = "JWT Access Token: " + accessToken + ", JWT Refresh Token: " + refreshToken;
        } else {
            responseMessage = "Authentication failed for email: " + email;
        }

        // CompletableFuture에 응답 설정
        CompletableFuture<String> responseFuture = responseFutures.remove(email);
        if (responseFuture != null) {
            responseFuture.complete(responseMessage);
        }
    }

    private String extractStatusFromMessage(String message) {
        return message.split("\"status\":\"")[1].split("\"")[0];
    }

    private String extractEmailFromMessage(String message) {
        return message.split("\"email\":\"")[1].split("\"")[0];
    }
}