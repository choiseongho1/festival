package com.festival.user.common.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import com.festival.user.common.enums.LoginStatus;

/**
 * 로그인 이벤트를 Kafka로 발행하는 프로듀서 클래스
 */
@Service
@Slf4j
public class LoginEventProducer {

    private static final String TOPIC = "login-responses";  // Kafka 토픽 이름 정의 (로그인 응답용)

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;  // Kafka 메시지를 전송하기 위한 KafkaTemplate

    /**
     * 로그인 결과를 Kafka 메시지로 발행
     * 
     * @param username 사용자 이메일/아이디
     * @param status 로그인 처리 결과 상태
     */
    public void sendLoginEvent(String username, LoginStatus status) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("status", status.getCode());
            messageMap.put("message", status.getMessage());
            messageMap.put("email", username);
            messageMap.put("eventType", "LOGIN_EVENT");
            
            String message = mapper.writeValueAsString(messageMap);
            log.info("Sending login event: {}", message);
            
            // 비동기로 메시지 전송 및 결과 처리
            kafkaTemplate.send(TOPIC, message)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Successfully sent login event for user: {}", username);
                    } else {
                        log.error("Failed to send login event: {}", ex.getMessage());
                    }
                });
        } catch (Exception e) {
            log.error("Error sending login event: {}", e.getMessage(), e);
        }
    }
}