package com.festival.match.common.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.festival.match.domain.Match;
import com.festival.match.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Slf4j
@RequiredArgsConstructor
public class MatchResponseConsumer {

    private final ObjectMapper objectMapper;
    private final MatchService matchService;

    @KafkaListener(topics = "match-requests", groupId = "match-service")
    public void consumeMatchRequest(String message) {
        try {
            log.info("매치 요청 메시지 수신: {}", message);
            
            JsonNode jsonNode = objectMapper.readTree(message);
            
            // eventType 검증
            if (!"MATCH_REQUEST".equals(jsonNode.get("eventType").asText())) {
                log.warn("지원하지 않는 이벤트 타입: {}", jsonNode.get("eventType").asText());
                return;
            }

            String leaderId = jsonNode.get("leaderId").asText();
            
            // 매치 엔티티 생성
            Match match = Match.builder()
                    .leaderId(leaderId)
                    .memberIds(Collections.singletonList(leaderId))
                    .status("PENDING")
                    .score(0)
                    .build();

            // 매치 생성
            Match savedMatch = matchService.createMatch(match);
            log.info("매치 생성 완료. MatchId: {}, LeaderId: {}", savedMatch.getId(), leaderId);
            
        } catch (JsonProcessingException e) {
            log.error("메시지 파싱 중 오류 발생: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("매치 처리 중 오류 발생: {}", e.getMessage(), e);
        }
    }
} 