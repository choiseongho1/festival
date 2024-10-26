package com.festival.user.service;

import com.festival.user.common.util.JwtUtil;
import com.festival.user.domain.User;
import com.festival.user.dto.UserLoginDto;
import com.festival.user.dto.UserSaveDto;
import com.festival.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;  // 실제 서비스 객체

    @Mock
    private UserRepository userRepository;  // Mock 객체

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Mock
    private JwtUtil jwtUtil; // Jwt Util

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Mock 객체 초기화
    }

    @Test
    public void testRegisterUser_Success() {
        // 1. 가짜 데이터 준비
        UserSaveDto userSaveDto = UserSaveDto.builder()
                .username("testuser")
                .inputPassword("password")
                .fullName("Test User")
                .phoneNumber("123-456-7890")
                .phoneVerified(false)
                .build();

        // 2. 사용자 중복 검사 Mock 설정 (사용자 없음)
        when(userRepository.findByUsername(userSaveDto.getUsername())).thenReturn(null);

        // 3. 저장할 사용자 엔티티 Mock 설정
        User savedUser = User.builder()
                .username("testuser")
                .password(passwordEncoder.encode("password"))
                .fullName("Test User")
                .phoneNumber("123-456-7890")
                .phoneVerified(false)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // 4. 테스트 수행
        User result = userService.registerUser(userSaveDto);

        // 5. 검증
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertTrue(passwordEncoder.matches("password", result.getPassword()));  // 비밀번호 암호화 검증

        // 6. userRepository.save()가 호출되었는지 확인
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegisterUser_UsernameAlreadyExists() {
        // 1. 첫 번째 사용자 데이터 준비
        UserSaveDto userSaveDto = UserSaveDto.builder()
                .username("testuser")
                .inputPassword("password")
                .fullName("Test User")
                .phoneNumber("123-456-7890")
                .phoneVerified(false)
                .build();

        // 2. 사용자 중복 검사 Mock 설정 (처음에는 사용자 없음)
        when(userRepository.findByUsername(userSaveDto.getUsername())).thenReturn(null);

        // 3. 첫 번째 사용자 등록
        userService.registerUser(userSaveDto);

        // 4. 두 번째 사용자 등록 시도 (이미 존재하는 사용자)
        UserSaveDto userSaveDtoFail = UserSaveDto.builder()
                .username("testuser")
                .inputPassword("password")
                .fullName("Test User")
                .phoneNumber("123-456-7890")
                .phoneVerified(false)
                .build();

        // 중복된 사용자 반환 설정
        when(userRepository.findByUsername(userSaveDtoFail.getUsername())).thenReturn(new User());

        // 5. 예외 발생 테스트
        assertThrows(RuntimeException.class, () -> {
            userService.registerUser(userSaveDtoFail);
        });

        // 6. userRepository.save()가 호출되지 않았는지 확인
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testLoginUser_Success() {
        // 1. 가짜 데이터 준비
        String rawPassword = "password";
        String accessToken = "access_token";
        String refreshToken = "refresh_token";

        User user = User.builder()
                .username("testuser")
                .password(passwordEncoder.encode(rawPassword))
                .fullName("Test User")
                .phoneNumber("123-456-7890")
                .phoneVerified(false)
                .build();

        // 2. Mock 설정
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(jwtUtil.generateToken("testuser", "ACCESS")).thenReturn(accessToken);
        when(jwtUtil.generateToken("testuser", "REFRESH")).thenReturn(refreshToken);

        // 3. 테스트 수행
        UserLoginDto loginDto = UserLoginDto.builder()
                .username("testuser")
                .inputPassword(rawPassword)
                .build();

        Map<String, String> result = userService.loginUser(loginDto);

        // 4. 검증
        assertEquals(result.get("accessToken"), accessToken);
        assertEquals(result.get("refreshToken"), refreshToken);
    }

    @Test
    public void testLoginUser_UserNotFound() {
        // 1. Mock 설정 - 사용자 조회 결과가 null로 설정
        when(userRepository.findByUsername("testuser")).thenReturn(null);

        // 2. 예외 발생 테스트 - RuntimeException이 발생하는지 확인
        assertThrows(RuntimeException.class, () -> {
            UserLoginDto loginDto = UserLoginDto.builder()
                    .username("testuser")
                    .inputPassword("password")
                    .build();
            userService.loginUser(loginDto);
        });
    }

    @Test
    public void testLoginUser_InvalidPassword() {
        // 1. 가짜 사용자 데이터 준비 - 올바른 비밀번호로 설정
        User user = User.builder()
                .username("testuser")
                .password(passwordEncoder.encode("correctpassword"))
                .fullName("Test User")
                .phoneNumber("123-456-7890")
                .phoneVerified(false)
                .build();

        // 2. Mock 설정 - 사용자 조회 결과 반환
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        // 3. 예외 발생 테스트 - 잘못된 비밀번호 입력 시 RuntimeException 발생 확인
        assertThrows(RuntimeException.class, () -> {
            UserLoginDto loginDto = UserLoginDto.builder()
                    .username("testuser")
                    .inputPassword("wrongpassword")
                    .build();
            userService.loginUser(loginDto);
        });
    }

}