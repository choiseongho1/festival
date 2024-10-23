package com.festival.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.festival.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

@DataMongoTest  // MongoDB 관련 테스트를 위한 어노테이션
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateAndFindUser() {
        // 1. 사용자 객체 생성
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setFullName("Test User");
        user.setPhoneNumber("123-456-7890");
        user.setPhoneVerified(true);

        // 2. MongoDB에 저장
        userRepository.save(user);

        // 3. 저장된 사용자 찾기
        Optional<User> opUser = userRepository.findById(user.getId());

        // 4. 사용자 데이터가 정상적으로 저장되었는지 검증
        assertThat(opUser).isPresent();
        assertThat(opUser.get().getUsername()).isEqualTo("testuser");
    }
}