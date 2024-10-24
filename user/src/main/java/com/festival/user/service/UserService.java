package com.festival.user.service;

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

    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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

    // 로그인 로직
    public String loginUser(UserLoginDto userLoginDto) {
        // 사용자 조회
        User user = userRepository.findByUsername(userLoginDto.getUsername());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(userLoginDto.getInputPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // 로그인 성공 시 메시지 반환
        return "Login successful!";
    }
}
