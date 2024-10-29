package com.festival.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.festival.user.common.kafka.LoginEventProducer;
import com.festival.user.domain.User;
import com.festival.user.dto.UserSaveDto;
import com.festival.user.dto.UserLoginDto;
import com.festival.user.repository.UserRepository;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private LoginEventProducer loginEventProducer;  // Kafka 프로듀서 mock 처리

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void registerUser_Success() {
        // given
        UserSaveDto userSaveDto = new UserSaveDto();
        userSaveDto.setUsername("testUser");
        userSaveDto.setInputPassword("password123");

        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        // when
        User savedUser = userService.registerUser(userSaveDto);

        // then
        assertNotNull(savedUser);
        assertEquals("testUser", savedUser.getUsername());
        assertEquals("encodedPassword", savedUser.getPassword());
    }

    @Test
    @DisplayName("중복 회원 가입 시 예외 발생 테스트")
    void registerUser_DuplicateUsername() {
        // given
        UserSaveDto userSaveDto = new UserSaveDto();
        userSaveDto.setUsername("testUser");
        userSaveDto.setInputPassword("password123");
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        // when
        userService.registerUser(userSaveDto);

        // then
        assertThrows(RuntimeException.class, () -> {
            userService.registerUser(userSaveDto);
        });
    }
}
