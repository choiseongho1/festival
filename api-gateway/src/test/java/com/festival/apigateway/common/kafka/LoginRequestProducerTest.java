package com.festival.apigateway.common.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
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

@SpringBootTest
@EmbeddedKafka(topics = "login-requests")
class LoginRequestProducerTest {

    private static final Logger log = LoggerFactory.getLogger(LoginRequestProducerTest.class);

    @Autowired
    private LoginRequestProducer producer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private static final String TOPIC = "login-requests";

    @Test
    void testSendLoginRequestToKafka() throws InterruptedException {
        // given
        BlockingQueue<ConsumerRecord<String, String>> messages = new LinkedBlockingQueue<>();
        
        var consumerFactory = new DefaultKafkaConsumerFactory<String, String>(
            KafkaTestUtils.consumerProps("test-group", "false", embeddedKafkaBroker)
        );
        
        var container = new KafkaMessageListenerContainer<>(
            consumerFactory, 
            new ContainerProperties(TOPIC)
        );
        
        container.setupMessageListener((MessageListener<String, String>) message -> {
            log.info("테스트 컨슈머가 메시지를 수신했습니다. 토픽: {}, 파티션: {}, 메시지: {}", 
                message.topic(), message.partition(), message.value());
            messages.add(message);
        });
        
        container.start();
        log.info("Kafka 리스너 컨테이너가 시작되었습니다.");

        // when
        String username = "testUser";
        String password = "testPass123";
        log.info("로그인 요청 메시지 전송 시도: username={}", username);
        producer.sendLoginRequest(username, password);

        // then
        ConsumerRecord<String, String> received = messages.poll(5, TimeUnit.SECONDS);
        
        if (received != null) {
            log.info("메시지 수신 성공! 수신된 메시지: {}", received.value());
            assertThat(received.value())
                .contains(username)
                .contains(password);
        } else {
            log.error("5초 동안 메시지를 수신하지 못했습니다.");
            assertThat(received).isNotNull();
        }

        container.stop();
        log.info("Kafka 리스너 컨테이너가 종료되었습니다.");
    }
} 