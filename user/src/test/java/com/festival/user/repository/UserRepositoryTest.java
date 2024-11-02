package com.festival.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.festival.user.domain.User;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

@DataMongoTest // MongoDB 관련 테스트를 위한 어노테이션
@Slf4j
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindById_ValidUser_ReturnsUser() {
        // given
        User user = User.builder()
                .username("testuser")
                .password("password")
                .fullName("Test User")
                .phoneNumber("123-456-7890")
                .phoneVerified(true)
                .build();
        log.info("테스트 시작: 새로운 사용자 생성");

        // when
        userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(user.getId());

        // then
        log.info("조회된 사용자: {}", foundUser);
        assertThat(foundUser)
                .isPresent()
                .get()
                .satisfies(u -> {
                    assertThat(u.getUsername()).isEqualTo("testuser");
                    assertThat(u.getFullName()).isEqualTo("Test User");
                    assertThat(u.getPhoneNumber()).isEqualTo("123-456-7890");
                    assertThat(u.isPhoneVerified()).isTrue();
                });
        log.info("사용자 정보가 성공적으로 저장되고 조회되었습니다.");
    }

    @Test
    void findByUsername_ExistingUser_ReturnsUser() {
        // given
        String username = "testUser";
        User user = User.builder()
                .username(username)
                .password("password")
                .build();
        userRepository.save(user);
        log.info("테스트 시작: 기존 사용자 username으로 조회");

        // when
        User foundUser = userRepository.findByUsername(username);

        // then
        log.info("조회된 사용자: {}", foundUser);
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo(username);
        log.info("사용자가 성공적으로 조회되었습니다.");
    }

    @Test
    void findByUsername_NonExistingUser_ReturnsNull() {
        // given
        String nonExistentUsername = "nonExistentUser";
        log.info("테스트 시작: 존재하지 않는 username으로 조회");

        // when
        User foundUser = userRepository.findByUsername(nonExistentUsername);

        // then
        log.info("조회 결과: {}", foundUser);
        assertThat(foundUser).isNull();
        log.info("존재하지 않는 사용자 조회 시 null이 반환되었습니다.");
    }
}