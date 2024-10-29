package com.festival.apigateway.common.kafka;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LoginRequestProducer {


    private static final String TOPIC = "login-requests";  // Kafka 토픽 이름 (로그인 요청용)

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;  // Kafka 메시지 전송을 위한 KafkaTemplate

    /**
     * 로그인 요청을 Kafka에 발행하는 메서드
     * @param email 사용자의 이메일
     * @param password 사용자의 비밀번호
     */
    public void sendLoginRequest(String username, String password) {
        // JSON 형태로 이메일과 비밀번호를 메시지로 변환하여 Kafka에 전송
        String message = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";
        kafkaTemplate.send(TOPIC, message);
    }
}