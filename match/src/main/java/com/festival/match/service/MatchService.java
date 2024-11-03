package com.festival.match.service;

import com.festival.match.domain.Match;
import com.festival.match.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;

    /**
     * 새로운 매치를 생성합니다.
     * 
     * @param match 생성할 매치 정보
     * @return 저장된 매치 정보
     * @throws IllegalArgumentException 필수 정보가 누락된 경우
     */
    public Match createMatch(Match match) {
        // 필수 필드 검증
        validateMatch(match);
        
        log.info("새로운 매치 생성 시작. LeaderId: {}", match.getLeaderId());

        try {
            Match savedMatch = matchRepository.save(match);
            log.info("매치 생성 완료. MatchId: {}, LeaderId: {}", 
                    savedMatch.getId(), savedMatch.getLeaderId());
            return savedMatch;
            
        } catch (Exception e) {
            log.error("매치 생성 중 오류 발생. LeaderId: {}, Error: {}", 
                    match.getLeaderId(), e.getMessage());
            throw new RuntimeException("매치 생성 실패", e);
        }
    }

    /**
     * 매치 정보의 유효성을 검증합니다.
     */
    private void validateMatch(Match match) {
        if (match == null) {
            throw new IllegalArgumentException("매치 정보가 null일 수 없습니다.");
        }
        if (StringUtils.isEmpty(match.getLeaderId())) {
            throw new IllegalArgumentException("리더 ID는 필수입니다.");
        }
        if (match.getMemberIds() == null || match.getMemberIds().isEmpty()) {
            throw new IllegalArgumentException("멤버 목록은 비어있을 수 없습니다.");
        }
        if (StringUtils.isEmpty(match.getStatus())) {
            throw new IllegalArgumentException("매치 상태는 필수입니다.");
        }
    }
}
