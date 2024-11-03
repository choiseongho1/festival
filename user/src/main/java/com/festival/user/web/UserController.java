package com.festival.user.web;

import com.festival.user.domain.User;
import com.festival.user.dto.UserLoginDto;
import com.festival.user.dto.UserSaveDto;
import com.festival.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "User Server is running successfully";
    }

    // 회원가입
    @PostMapping("/register")
    public User registerUser(@RequestBody UserSaveDto userSaveDto) {
        return userService.registerUser(userSaveDto);
    }

    /**
     * 사용자의 휴대폰 인증 상태를 확인하는 API
     */
    @GetMapping("/{userId}/verification/status")
    public ResponseEntity<Boolean> isPhoneVerified(@PathVariable String userId) {
        log.info("휴대폰 인증 상태 확인 요청. UserId: {}", userId);
        boolean isVerified = userService.isPhoneVerified(userId);
        log.info("휴대폰 인증 상태 확인 완료. UserId: {}, isVerified: {}", userId, isVerified);
        return ResponseEntity.ok(isVerified);
    }
}
