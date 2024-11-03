package com.festival.group.common.kafka;

import com.festival.group.domain.Group;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class MatchRequestProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private MatchRequestProducer matchRequestProducer;

    @Test
    void sendMatchRequestEvent_Success() {
        // given
        Group group = Group.builder()
                .id(1L)
                .leaderId("user123")
                .groupName("테스트 그룹")
                .build();

        // CompletableFuture를 모킹하여 성공 시나리오 설정
        CompletableFuture<SendResult<String, String>> future = new CompletableFuture<>();
        SendResult<String, String> sendResult = mock(SendResult.class);
        future.complete(sendResult);

        // kafkaTemplate이 메시지를 보낼 때 future를 반환하도록 설정
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(future);
        
        log.info("테스트 시작: 매치 요청 이벤트 전송");

        // when
        matchRequestProducer.sendMatchRequestEvent(group);

        // then
        verify(kafkaTemplate).send(eq("match-requests"), argThat(message -> {
            log.info("전송된 메시지: {}", message);
            return message.contains("\"groupId\":\"1\"") &&
                   message.contains("\"leaderId\":\"user123\"") &&
                   message.contains("\"eventType\":\"MATCH_REQUEST\"");
        }));
        
        log.info("매치 요청 이벤트가 성공적으로 전송되었습니다.");
    }
} 