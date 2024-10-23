package com.festival.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/user/health-check").permitAll()  // health-check 엔드포인트는 인증 없이 접근 가능
                        .anyRequest().authenticated()  // 나머지 모든 요청은 인증 필요
                )
                .formLogin(form -> form.permitAll())  // 로그인 페이지는 인증 없이 접근 가능
                .logout(logout -> logout.permitAll());  // 로그아웃도 인증 없이 접근 가능

        return http.build();
    }
}