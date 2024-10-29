package com.festival.user.common.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LoginEventProducer {

    private static final String TOPIC = "login-requests";  // Kafka 토픽 이름 정의 (로그인 요청용)

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;  // Kafka 메시지를 전송하기 위한 KafkaTemplate

    /**
     * 로그인 이벤트를 Kafka에 발행하는 메서드
     * @param username 로그인에 성공한 사용자의 이름
     */
    public void sendLoginEvent(String username) {
        // username을 Kafka 메시지로 변환하여 login-requests 토픽에 전송
        kafkaTemplate.send(TOPIC, username);
    }
}