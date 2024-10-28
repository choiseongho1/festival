package com.festival.user.service;

import com.festival.user.common.util.JwtUtil;
import com.festival.user.domain.User;
import com.festival.user.dto.UserLoginDto;
import com.festival.user.dto.UserSaveDto;
import com.festival.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    // 사용자 레포지토리
    private final UserRepository userRepository;

    // 비밀번호 인코더
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // JWT 관련 Util
    private final JwtUtil jwtUtil;




    // 회원가입 로직
    public User registerUser(UserSaveDto userSaveDto) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userSaveDto.getInputPassword());

        // 사용자 중복 체크
        User findUser = userRepository.findByUsername(userSaveDto.getUsername());

        if (findUser != null) {
            // TODO Exception Handler 추가하여 변경 필요
            throw new RuntimeException();
        }

        // 암호화 비밀번호 set
        userSaveDto.setEncodePassword(encodedPassword);
        // 엔티티로 변환하여 저장
        User saveUser = userRepository.save(userSaveDto.toEntity());
        return saveUser;
    }

    // 사용자 로그인
    public Map<String, String> loginUser(UserLoginDto userLoginDto) {
        User user = userRepository.findByUsername(userLoginDto.getUsername());
        if (user == null || !passwordEncoder.matches(userLoginDto.getInputPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        String accessToken = jwtUtil.generateToken(user.getUsername(), "ACCESS");
        String refreshToken = jwtUtil.generateToken(user.getUsername(), "REFRESH");

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }
<<<<<<< HEAD

=======
>>>>>>> e97c9392a588289923946d6d6a7f80e718dde0b2
}
