package com.festival.user.service;

import com.festival.user.common.kafka.LoginEventProducer;
import com.festival.user.domain.User;
import com.festival.user.dto.UserLoginDto;
import com.festival.user.dto.UserSaveDto;
import com.festival.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private LoginEventProducer loginEventProducer;

    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    
    @Test
    void authenticate_ValidCredentials_ReturnsTrue() {
        // given
        String username = "testUser";
        String password = "testPassword";
        String encodedPassword = passwordEncoder.encode(password);

        User mockUser = User.builder()
                .username(username)
                .password(encodedPassword)
                .build();

        UserLoginDto loginDto = UserLoginDto.builder()
                .username(username)
                .inputPassword(password)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(mockUser);
        log.info("테스트 시작: 올바른 자격 증명으로 인증 시도");

        // when
        boolean result = userService.authenticate(loginDto);

        // then
        log.info("인증 결과: {}", result);
        assertThat(result).isTrue();
        verify(loginEventProducer).sendLoginEvent(username);
        log.info("로그인 이벤트가 성공적으로 발행되었습니다.");
    }

    @Test
    void authenticate_InvalidPassword_ReturnsFalse() {
        // given
        String username = "testUser";
        String correctPassword = "correctPassword";
        String wrongPassword = "wrongPassword";
        String encodedPassword = passwordEncoder.encode(correctPassword);

        User mockUser = User.builder()
                .username(username)
                .password(encodedPassword)
                .build();

        UserLoginDto loginDto = UserLoginDto.builder()
                .username(username)
                .inputPassword(wrongPassword)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(mockUser);
        log.info("테스트 시작: 잘못된 비밀번호로 인증 시도");

        // when
        boolean result = userService.authenticate(loginDto);

        // then
        log.info("인증 결과: {}", result);
        assertThat(result).isFalse();
        verify(loginEventProducer, never()).sendLoginEvent(anyString());
        log.info("잘못된 비밀번호로 인해 로그인 이벤트가 발행되지 않았습니다.");
    }

    @Test
    void authenticate_UserNotFound_ReturnsFalse() {
        // given
        String username = "nonExistentUser";
        String password = "anyPassword";

        UserLoginDto loginDto = UserLoginDto.builder()
                .username(username)
                .inputPassword(password)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(null);
        log.info("테스트 시작: 존재하지 않는 사용자로 인증 시도");

        // when
        boolean result = userService.authenticate(loginDto);

        // then
        log.info("인증 결과: {}", result);
        assertThat(result).isFalse();
        verify(loginEventProducer, never()).sendLoginEvent(anyString());
        log.info("존재하지 않는 사용자로 인해 로그인 이벤트가 발행되지 않았습니다.");
    }

    @Test
    void registerUser_Success() {
        // given
        String username = "testUser";
        String password = "password123";
        String encodedPassword = "encodedPassword";

        UserSaveDto userSaveDto = UserSaveDto.builder()
                .username(username)
                .inputPassword(password)
                .build();

        User mockUser = User.builder()
                .username(username)
                .password(encodedPassword)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        log.info("테스트 시작: 신규 회원 가입 시도");

        // when
        User savedUser = userService.registerUser(userSaveDto);

        // then
        log.info("회원가입 결과 - username: {}", savedUser.getUsername());
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(username);
        assertThat(savedUser.getPassword()).isEqualTo(encodedPassword);
        verify(userRepository).findByUsername(username);
        verify(userRepository).save(any(User.class));
        log.info("회원가입이 성공적으로 완료되었습니다.");
    }

    @Test
    void registerUser_DuplicateUsername_ThrowsException() {
        // given
        String username = "testUser";
        String password = "password123";

        UserSaveDto userSaveDto = UserSaveDto.builder()
                .username(username)
                .inputPassword(password)
                .build();

        User existingUser = User.builder()
                .username(username)
                .password("alreadyEncodedPassword")
                .build();

        when(userRepository.findByUsername(username)).thenReturn(existingUser);
        log.info("테스트 시작: 중복된 사용자명으로 회원가입 시도");

        // when & then
        assertThatThrownBy(() -> userService.registerUser(userSaveDto))
                .isInstanceOf(RuntimeException.class);
        
        verify(userRepository).findByUsername(username);
        verify(userRepository, never()).save(any(User.class));
        log.info("예상대로 중복 회원 가입 시도가 RuntimeException으로 차단되었습니다.");
    }
}
