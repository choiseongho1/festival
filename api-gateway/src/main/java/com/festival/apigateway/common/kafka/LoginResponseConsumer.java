package com.festival.apigateway.common.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.festival.apigateway.common.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 로그인 응답을 처리하는 Kafka 컨슈머 클래스
 * 인증 결과에 따라 JWT 토큰 생성 또는 에러 메시지 반환
 */
@Service
@Slf4j
public class LoginResponseConsumer {

    @Autowired
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 이메일별 CompletableFuture를 저장하는 ConcurrentHashMap
    private final Map<String, CompletableFuture<String>> responseFutures = new ConcurrentHashMap<>();

    /**
     * 응답 대기 Future 설정 및 타임아웃 처리
     * 30초 후 자동으로 타임아웃 처리됨
     *
     * @param email 사용자 이메일
     * @param responseFuture 응답을 기다리는 CompletableFuture 객체
     */
    public void setResponseFuture(String email, CompletableFuture<String> responseFuture) {
        responseFutures.put(email, responseFuture);
        
        // 30초 후에 타임아웃 처리
        CompletableFuture.delayedExecutor(30, TimeUnit.SECONDS).execute(() -> {
            CompletableFuture<String> future = responseFutures.remove(email);
            if (future != null && !future.isDone()) {
                String timeoutResponse = String.format(
                    "{\"status\":\"timeout\",\"message\":\"로그인 요청 시간이 초과되었습니다\"}"
                );
                future.complete(timeoutResponse);
            }
        });
    }

    /**
     * 로그인 응답 메시지 처리
     * 성공 시 JWT 토큰 생성, 실패 시 에러 메시지 반환
     *
     * @param message Kafka로부터 수신한 로그인 응답 메시지
     */
    @KafkaListener(topics = "login-responses", groupId = "api-gateway-group")
    public void processLoginResponse(String message) {
        log.info("Received message: {}", message);
        
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String status = jsonNode.path("status").asText();
            String email = jsonNode.path("email").asText();
            String statusMessage = jsonNode.path("message").asText();

            String responseMessage;
            if ("success".equals(status)) {
                String accessToken = jwtUtil.generateToken(email, "ACCESS");
                String refreshToken = jwtUtil.generateToken(email, "REFRESH");
                responseMessage = String.format(
                    "{\"status\":\"%s\",\"message\":\"%s\",\"accessToken\":\"%s\",\"refreshToken\":\"%s\"}", 
                    status, statusMessage, accessToken, refreshToken
                );
            } else {
                responseMessage = String.format(
                    "{\"status\":\"%s\",\"message\":\"%s\"}", 
                    status, statusMessage
                );
            }

            CompletableFuture<String> responseFuture = responseFutures.remove(email);
            if (responseFuture != null) {
                log.info("Completing responseFuture for email: {}", email);
                responseFuture.complete(responseMessage);
            } else {
                log.warn("No waiting future found for email: {}", email);
            }
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }
}