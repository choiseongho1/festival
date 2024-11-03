package com.festival.match.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festival.match.common.kafka.MatchResponseConsumer;
import com.festival.match.domain.Match;
import com.festival.match.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class MatchResponseConsumerTest {

    @Mock
    private MatchService matchService;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private MatchResponseConsumer matchResponseConsumer;

    @Test
    void consumeMatchRequest_ValidMessage_Success() {
        // given
        // 실제 전송되는 메시지 형식과 동일한 테스트 메시지
        String message = "{\"groupId\":\"1\",\"eventType\":\"MATCH_REQUEST\",\"leaderId\":\"user123\"}";
        
        // 저장될 것으로 예상되는 Match 객체
        Match expectedMatch = Match.builder()
                .leaderId("user123")
                .memberIds(Collections.singletonList("user123"))
                .status("PENDING")
                .score(0)
                .build();

        // matchService가 반환할 저장된 Match 객체
        Match savedMatch = Match.builder()
                .id("1")
                .leaderId("user123")
                .memberIds(Collections.singletonList("user123"))
                .status("PENDING")
                .score(0)
                .build();

        when(matchService.createMatch(any(Match.class))).thenReturn(savedMatch);
        
        log.info("테스트 시작: Kafka 메시지 수신 테스트");
        log.info("입력 메시지: {}", message);

        // when
        matchResponseConsumer.consumeMatchRequest(message);

        // then
        verify(matchService).createMatch(argThat(match -> {
            log.info("생성된 매치 정보 검증");
            log.info("리더 ID: {}", match.getLeaderId());
            log.info("멤버 목록: {}", match.getMemberIds());
            log.info("매치 상태: {}", match.getStatus());
            
            return match.getLeaderId().equals("user123") &&
                   match.getMemberIds().size() == 1 &&
                   match.getMemberIds().contains("user123") &&
                   match.getStatus().equals("PENDING") &&
                   match.getScore() == 0;
        }));
        
        log.info("매치 요청 처리 완료");
    }

    @Test
    void consumeMatchRequest_WrongEventType_IgnoresMessage() {
        // given
        String message = "{\"groupId\":\"1\",\"eventType\":\"WRONG_TYPE\",\"leaderId\":\"user123\"}";
        log.info("테스트 시작: 잘못된 이벤트 타입 테스트");
        log.info("입력 메시지: {}", message);

        // when
        matchResponseConsumer.consumeMatchRequest(message);

        // then
        verify(matchService, never()).createMatch(any());
        log.info("잘못된 이벤트 타입으로 인해 매치 생성이 무시됨");
    }

    @Test
    void consumeMatchRequest_InvalidJson_HandlesError() {
        // given
        String invalidMessage = "invalid json";
        log.info("테스트 시작: 잘못된 JSON 형식 테스트");
        log.info("입력 메시지: {}", invalidMessage);

        // when
        matchResponseConsumer.consumeMatchRequest(invalidMessage);

        // then
        verify(matchService, never()).createMatch(any());
        log.info("잘못된 JSON 형식으로 인해 매치 생성이 실패함");
    }

    @Test
    void consumeMatchRequest_MissingRequiredField_HandlesError() {
        // given
        String message = "{\"groupId\":\"1\",\"eventType\":\"MATCH_REQUEST\"}"; // leaderId 누락
        log.info("테스트 시작: 필수 필드 누락 테스트");
        log.info("입력 메시지: {}", message);

        // when
        matchResponseConsumer.consumeMatchRequest(message);

        // then
        verify(matchService, never()).createMatch(any());
        log.info("필수 필드 누락으로 인해 매치 생성이 실패함");
    }
} 