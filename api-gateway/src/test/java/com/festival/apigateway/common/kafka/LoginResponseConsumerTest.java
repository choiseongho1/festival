package com.festival.apigateway.common.kafka;

import com.festival.apigateway.common.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@EmbeddedKafka(topics = "login-responses")
class LoginResponseConsumerTest {

    private static final Logger log = LoggerFactory.getLogger(LoginResponseConsumerTest.class);

    @Autowired
    private LoginResponseConsumer consumer;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void testProcessLoginResponse() throws Exception {
        // given
        String testEmail = "test@example.com";
        String successMessage = "{\"status\":\"success\",\"email\":\"" + testEmail + "\"}";
        
        // JWT 토큰 모의 설정
        when(jwtUtil.generateToken(anyString(), anyString()))
            .thenReturn("mocked.jwt.token");

        // CompletableFuture 설정
        CompletableFuture<String> future = new CompletableFuture<>();
        consumer.setResponseFuture(testEmail, future);
        
        log.info("테스트 시작: 로그인 응답 메시지 전송");

        // when
        kafkaTemplate.send("login-responses", successMessage);
        
        // then
        String result = future.get(5, TimeUnit.SECONDS);
        log.info("수신된 응답: {}", result);
        
        assertThat(result)
            .contains("JWT Access Token")
            .contains("JWT Refresh Token")
            .contains("mocked.jwt.token");
    }

    @Test
    void testProcessLoginResponse_Failure() throws Exception {
        // given
        String testEmail = "test@example.com";
        String failureMessage = "{\"status\":\"failure\",\"email\":\"" + testEmail + "\"}";
        
        CompletableFuture<String> future = new CompletableFuture<>();
        consumer.setResponseFuture(testEmail, future);
        
        log.info("테스트 시작: 로그인 실패 응답 메시지 전송");

        // when
        kafkaTemplate.send("login-responses", failureMessage);
        
        // then
        String result = future.get(5, TimeUnit.SECONDS);
        log.info("수신된 실패 응답: {}", result);
        
        assertThat(result)
            .contains("Authentication failed")
            .contains(testEmail);
    }
} 