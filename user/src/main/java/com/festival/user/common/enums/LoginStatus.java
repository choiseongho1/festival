package com.festival.user.common.enums;

/**
 * 로그인 처리 상태를 정의하는 열거형
 * 각 상태별 코드와 메시지를 포함
 */
public enum LoginStatus {
    SUCCESS("success", "로그인 성공"),
    INVALID_CREDENTIALS("invalid_credentials", "아이디 또는 비밀번호가 일치하지 않습니다"),
    USER_NOT_FOUND("user_not_found", "존재하지 않는 사용자입니다"),
    TIMEOUT("timeout", "로그인 요청 시간이 초과되었습니다");

    private final String code;    // 상태 코드
    private final String message; // 사용자에게 표시할 메시지

    LoginStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
} 