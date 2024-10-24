package com.festival.user.web;

import com.festival.user.domain.User;
import com.festival.user.dto.UserLoginDto;
import com.festival.user.dto.UserSaveDto;
import com.festival.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
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

    // 로그인
    @PostMapping("/login")
    public String loginUser(@RequestBody UserLoginDto userLoginDto) {
        return userService.loginUser(userLoginDto);
    }
}
