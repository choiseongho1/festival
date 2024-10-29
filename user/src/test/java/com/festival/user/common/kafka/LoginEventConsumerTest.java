package com.festival.user.common.kafka;

import com.festival.user.dto.UserLoginDto;
import com.festival.user.service.UserService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@EmbeddedKafka(topics = {"login-requests", "login-responses"})
class LoginEventConsumerTest {

    private static final Logger log = LoggerFactory.getLogger(LoginEventConsumerTest.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @MockBean
    private UserService userService;

    private static final String REQUEST_TOPIC = "login-requests";
    private static final String RESPONSE_TOPIC = "login-responses";

    @Test
    void testConsumeLoginRequest() throws InterruptedException {
        // given
        BlockingQueue<ConsumerRecord<String, String>> messages = new LinkedBlockingQueue<>();
        
        var consumerFactory = new DefaultKafkaConsumerFactory<String, String>(
            KafkaTestUtils.consumerProps("test-group", "false", embeddedKafkaBroker)
        );
        
        var container = new KafkaMessageListenerContainer<>(
            consumerFactory, 
            new ContainerProperties(RESPONSE_TOPIC)
        );
        
        container.setupMessageListener((MessageListener<String, String>) message -> {
            log.info("테스트 컨슈머가 응답 메시지를 수신했습니다. 토픽: {}, 메시지: {}",
                message.topic(), message.value());
            messages.add(message);
        });
        
        container.start();
        log.info("Kafka 리스너 컨테이너가 시작되었습니다.");

        // UserService 모의 설정
        when(userService.authenticate(any(UserLoginDto.class))).thenReturn(true);

        // when
        String username = "testUser";
        String password = "testPass";
        String loginRequest = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
        
        log.info("로그인 요청 메시지 전송: {}", loginRequest);
        kafkaTemplate.send(REQUEST_TOPIC, loginRequest);

        // then
        ConsumerRecord<String, String> received = messages.poll(5, TimeUnit.SECONDS);
        
        if (received != null) {
            log.info("응답 메시지 수신 성공! 수신된 메시지: {}", received.value());
            assertThat(received.value())
                .contains("\"status\":\"success\"")
                .contains(username);
        } else {
            log.error("5초 동안 응답 메시지를 수신하지 못했습니다.");
            assertThat(received).isNotNull();
        }

        container.stop();
        log.info("Kafka 리스너 컨테이너가 종료되었습니다.");
    }

    @Test
    void testConsumeLoginRequest_AuthenticationFailed() throws InterruptedException {
        // given
        BlockingQueue<ConsumerRecord<String, String>> messages = new LinkedBlockingQueue<>();
        
        var consumerFactory = new DefaultKafkaConsumerFactory<String, String>(
            KafkaTestUtils.consumerProps("test-group", "false", embeddedKafkaBroker)
        );
        
        var container = new KafkaMessageListenerContainer<>(
            consumerFactory, 
            new ContainerProperties(RESPONSE_TOPIC)
        );
        
        container.setupMessageListener((MessageListener<String, String>) message -> {
            log.info("테스트 컨슈머가 실패 응답 메시지를 수신했습니다. 토픽: {}, 메시지: {}", 
                message.topic(), message.value());
            messages.add(message);
        });
        
        container.start();
        log.info("Kafka 리스너 컨테이너가 시작되었습니다.");

        // UserService 모의 설정 - 인증 실패
        when(userService.authenticate(any(UserLoginDto.class))).thenReturn(false);

        // when
        String username = "testUser";
        String password = "wrongPassword";
        String loginRequest = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
        
        log.info("잘못된 비밀번호로 로그인 요청 전송: {}", loginRequest);
        kafkaTemplate.send(REQUEST_TOPIC, loginRequest);

        // then
        ConsumerRecord<String, String> received = messages.poll(5, TimeUnit.SECONDS);
        
        if (received != null) {
            log.info("실패 응답 메시지 수신 성공! 수신된 메시지: {}", received.value());
            assertThat(received.value())
                .contains("\"status\":\"failure\"")
                .contains(username);
        } else {
            log.error("5초 동안 실패 응답 메시지를 수신하지 못했습니다.");
            assertThat(received).isNotNull();
        }

        container.stop();
        log.info("Kafka 리스너 컨테이너가 종료되었습니다.");
    }
} 