package com.festival.user.common.kafka;

import com.festival.user.dto.UserLoginDto;
import com.festival.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LoginEventConsumer {

    @Autowired
    private UserService userService;  // User 자격 검증을 위한 서비스

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;  // 로그인 결과를 다시 Kafka로 보내기 위한 Template

    private static final String RESPONSE_TOPIC = "login-responses";

    @KafkaListener(topics = "login-requests", groupId = "user-service-group")
    public void consumeLoginRequest(String message) {
        // 메시지를 파싱하여 이메일과 비밀번호 추출 (간단한 JSON 파싱 예시)
        String username = extractUsernameFromMessage(message);
        String password = extractPasswordFromMessage(message);

        UserLoginDto loginDto = UserLoginDto.builder().username(username).inputPassword(password).build();


        // 자격 검증
        boolean isAuthenticated = userService.authenticate(loginDto);
        String responseMessage = isAuthenticated ? "{\"status\":\"success\", \"username\":\"" + username + "\"}" :
                "{\"status\":\"failure\", \"username\":\"" + username + "\"}";

        // 인증 결과를 login-responses 토픽에 발행
        kafkaTemplate.send(RESPONSE_TOPIC, responseMessage);
    }

    private String extractUsernameFromMessage(String message) {
        // JSON 파싱 로직 (간단히 문자열 파싱 방식으로 작성)
        return message.split("\"username\":\"")[1].split("\"")[0];
    }

    private String extractPasswordFromMessage(String message) {
        return message.split("\"password\":\"")[1].split("\"")[0];
    }
}