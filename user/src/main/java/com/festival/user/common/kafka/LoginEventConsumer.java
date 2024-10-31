package com.festival.user.common.kafka;

import com.festival.user.common.enums.LoginStatus;
import com.festival.user.dto.UserLoginDto;
import com.festival.user.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class LoginEventConsumer {

    @Autowired
    private UserService userService;  // 사용자 인증 및 계정 관리를 위한 서비스

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;  // Kafka 메시지 발행을 위한 템플릿

    @Autowired
    private ObjectMapper objectMapper;  // JSON 처리를 위한 매퍼

    private static final String RESPONSE_TOPIC = "login-responses";

    /**
     * 로그인 요청 메시지를 소비하고 처리하는 메서드
     * @param message JSON 형식의 로그인 요청 메시지
     */
    @KafkaListener(topics = "login-requests", groupId = "user-service-group")
    public void consumeLoginRequest(String message) {
        log.info("Received login request: {}", message);
        
        try {
            // JSON 메시지 파싱
            JsonNode jsonNode = objectMapper.readTree(message);
            String username = jsonNode.path("username").asText();
            String password = jsonNode.path("password").asText();

            // 로그인 DTO 생성
            UserLoginDto loginDto = UserLoginDto.builder()
                .username(username)
                .inputPassword(password)
                .build();

            // 사용자 인증 수행
            LoginStatus loginStatus = userService.authenticate(loginDto);

            // 응답 메시지 생성 및 전송
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("status", loginStatus.getCode());
            responseMap.put("message", loginStatus.getMessage());
            responseMap.put("email", username);
            responseMap.put("eventType", "LOGIN_RESPONSE");

            String responseMessage = objectMapper.writeValueAsString(responseMap);
            
            // 응답 전송
            kafkaTemplate.send(RESPONSE_TOPIC, responseMessage)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Login response sent successfully for user: {}", username);
                    } else {
                        log.error("Failed to send login response: {}", ex.getMessage());
                    }
                });

        } catch (Exception e) {
            log.error("Error processing login request: {}", e.getMessage(), e);
        }
    }
}