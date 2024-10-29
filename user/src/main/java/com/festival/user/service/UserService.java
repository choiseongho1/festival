package com.festival.user.service;

import com.festival.user.common.kafka.LoginEventProducer;
import com.festival.user.common.util.JwtUtil;
import com.festival.user.domain.User;
import com.festival.user.dto.UserLoginDto;
import com.festival.user.dto.UserSaveDto;
import com.festival.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    // 사용자 레포지토리
    private final UserRepository userRepository;

    // 비밀번호 인코더
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // JWT 관련 Util
    private final JwtUtil jwtUtil;

    // Kafka 이벤트 발행용 프로듀서 주입
    private final LoginEventProducer loginEventProducer;



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


    /**
     * 사용자 자격을 검증하는 메서드 (인증)
     * @param userLoginDto 로그인 정보 (username, password)
     * @return 올바른 사용자 여부
     */
    public boolean authenticate(UserLoginDto userLoginDto) {
        User user = userRepository.findByUsername(userLoginDto.getUsername());

        if (user == null || !passwordEncoder.matches(userLoginDto.getInputPassword(), user.getPassword())) {
            return false;  // 사용자 인증 실패
        }

        // 사용자 인증 성공
        loginEventProducer.sendLoginEvent(user.getUsername());
        return true;
    }
}
