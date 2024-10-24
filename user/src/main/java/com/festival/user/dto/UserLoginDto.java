package com.festival.user.dto;
import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class UserLoginDto {

    private String username;  // 사용자 아이디 또는 이메일 (중복 불가)
    private String inputPassword;  // 입력 비밀번호

}
