package com.festival.match.repository;

import com.festival.match.domain.Match;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Slf4j
class MatchRepositoryTest {

    @Autowired
    private MatchRepository matchRepository;


    @Test
    void baseDomainIsRunning() {
        // given
        Match match = Match.builder()
                .leaderId("leader123")
                .memberIds(Arrays.asList("member1", "member2"))
                .festivalId("festival456")
                .score(100)
                .status("ACTIVE")
                .build();

        // when
        Match savedMatch = matchRepository.save(match);

        // then
        Optional<Match> foundMatch = matchRepository.findById(savedMatch.getId());
        assertThat(!Objects.isNull(foundMatch.get().getCreatedAt()));
    }

    @Test
    void saveAndFindMatch() {
        // given
        Match match = Match.builder()
                .leaderId("leader123")
                .memberIds(Arrays.asList("member1", "member2"))
                .festivalId("festival456")
                .score(100)
                .status("ACTIVE")
                .build();

        // when
        Match savedMatch = matchRepository.save(match);

        // then
        Optional<Match> foundMatch = matchRepository.findById(savedMatch.getId());
        assertThat(foundMatch).isPresent();
        assertThat(foundMatch.get().getLeaderId()).isEqualTo("leader123");
        assertThat(foundMatch.get().getMemberIds()).containsExactly("member1", "member2");
        assertThat(foundMatch.get().getFestivalId()).isEqualTo("festival456");
        assertThat(foundMatch.get().getScore()).isEqualTo(100);
        assertThat(foundMatch.get().getStatus()).isEqualTo("ACTIVE");
    }
}