package com.festival.group.common.kafka;

import com.festival.group.domain.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 매치 요청 이벤트를 Kafka로 발행하는 프로듀서 클래스
 */
@Service
@Slf4j
public class MatchRequestProducer {

    private static final String TOPIC = "match-requests"; // 매치 요청을 위한 Kafka 토픽 이름

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // Kafka 메시지를 전송하기 위한 KafkaTemplate

    /**
     * 매치 요청 이벤트를 Kafka 메시지로 발행
     * 
     * @param group 생성된 그룹 객체
     */
    public void sendMatchRequestEvent(Group group) {
        try {
            // 메시지를 JSON 형식으로 변환하기 위한 ObjectMapper
            ObjectMapper mapper = new ObjectMapper();
            // 메시지에 포함될 데이터 맵 생성
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("groupId", group.getId().toString()); // 그룹 ID 추가
            messageMap.put("leaderId", group.getLeaderId()); // 리더 ID 추가
            messageMap.put("eventType", "MATCH_REQUEST"); // 이벤트 타입 지정

            // 메시지를 JSON 문자열로 변환
            String message = mapper.writeValueAsString(messageMap);
            log.info("Sending match request event: {}", message);

            // 비동기로 메시지 전송 및 결과 처리
            kafkaTemplate.send(TOPIC, message)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Successfully sent match request event for group: {}", group.getId());
                    } else {
                        log.error("Failed to send match request event: {}", ex.getMessage());
                    }
                });
        } catch (Exception e) {
            log.error("Error sending match request event: {}", e.getMessage(), e);
        }
    }
}