package com.festival.match.service;

import com.festival.match.domain.Match;
import com.festival.match.repository.MatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    private static final Logger log = LoggerFactory.getLogger(MatchServiceTest.class);

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private MatchService matchService;

    @Test
    void createMatch_ValidInput_Success() {
        // given
        Match match = Match.builder()
                .leaderId("user123")
                .memberIds(Collections.singletonList("user123"))
                .status("PENDING")
                .score(0)
                .build();

        Match savedMatch = Match.builder()
                .id("1")
                .leaderId("user123")
                .memberIds(Collections.singletonList("user123"))
                .status("PENDING")
                .score(0)
                .build();

        when(matchRepository.save(any(Match.class))).thenReturn(savedMatch);
        
        log.info("테스트 시작: 유효한 매치 생성");

        // when
        Match result = matchService.createMatch(match);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getLeaderId()).isEqualTo("user123");
        assertThat(result.getMemberIds()).containsExactly("user123");
        assertThat(result.getStatus()).isEqualTo("PENDING");
        assertThat(result.getScore()).isEqualTo(0);
        
        verify(matchRepository).save(match);
        log.info("매치가 성공적으로 생성되었습니다. MatchId: {}", result.getId());
    }

    @Test
    void createMatch_NullMatch_ThrowsException() {
        // given
        Match nullMatch = null;
        log.info("테스트 시작: null 매치 생성 시도");

        // when & then
        assertThatThrownBy(() -> matchService.createMatch(nullMatch))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("매치 정보가 null일 수 없습니다.");
        
        verify(matchRepository, never()).save(any());
        log.info("예상대로 예외가 발생했습니다.");
    }

    @Test
    void createMatch_EmptyLeaderId_ThrowsException() {
        // given
        Match matchWithoutLeader = Match.builder()
                .memberIds(Collections.singletonList("user123"))
                .status("PENDING")
                .score(0)
                .build();
                
        log.info("테스트 시작: 리더 ID가 없는 매치 생성 시도");

        // when & then
        assertThatThrownBy(() -> matchService.createMatch(matchWithoutLeader))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("리더 ID는 필수입니다.");
        
        verify(matchRepository, never()).save(any());
        log.info("예상대로 예외가 발생했습니다.");
    }

    @Test
    void createMatch_EmptyMembers_ThrowsException() {
        // given
        Match matchWithoutMembers = Match.builder()
                .leaderId("user123")
                .status("PENDING")
                .score(0)
                .build();
                
        log.info("테스트 시작: 멤버가 없는 매치 생성 시도");

        // when & then
        assertThatThrownBy(() -> matchService.createMatch(matchWithoutMembers))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("멤버 목록은 비어있을 수 없습니다.");
        
        verify(matchRepository, never()).save(any());
        log.info("예상대로 예외가 발생했습니다.");
    }

    @Test
    void createMatch_EmptyStatus_ThrowsException() {
        // given
        Match matchWithoutStatus = Match.builder()
                .leaderId("user123")
                .memberIds(Collections.singletonList("user123"))
                .score(0)
                .build();
                
        log.info("테스트 시작: 상태가 없는 매치 생성 시도");

        // when & then
        assertThatThrownBy(() -> matchService.createMatch(matchWithoutStatus))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("매치 상태는 필수입니다.");
        
        verify(matchRepository, never()).save(any());
        log.info("예상대로 예외가 발생했습니다.");
    }
} 