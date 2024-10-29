package com.festival.apigateway.common.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class LoginRequestEvent {
    private String requestId;
    private String email;
    private String password;
    private LocalDateTime timestamp;
}