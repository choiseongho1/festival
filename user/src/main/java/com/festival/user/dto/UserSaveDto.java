package com.festival.user.dto;

import com.festival.user.domain.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class UserSaveDto {

    private String id;  // MongoDB에서 자동 생성되는 고유 ID
    private String username;  // 사용자 아이디 또는 이메일 (중복 불가)
    private String inputPassword;  // 입력 비밀번호
    private String encodePassword; // 암호화 비밀번호
    private String fullName;  // 사용자 이름
    private String phoneNumber;  // 연락처 (필수)
    private boolean phoneVerified;  // 전화번호 인증 여부
    private Set<String> roles;  // 권한 (ROLE_USER, ROLE_ADMIN 등)
    private String favoriteFestival;  // 선호하는 축제 (사용자 프로필용)
    private LocalDateTime createdAt;  // 사용자 등록 날짜
    private LocalDateTime updatedAt;  // 마지막 수정 날짜
    private boolean availableForMatching;  // 매칭 가능 여부
    private String preferredFestivalType;  // 선호하는 축제 종류 (음악, 문화 등)


    public User toEntity(){
        return User.builder()
                .id(this.id)
                .username(this.username)
                .password(this.encodePassword)
                .fullName(this.fullName)
                .phoneNumber(this.phoneNumber)
                .phoneVerified(this.phoneVerified)
                .favoriteFestival(this.favoriteFestival)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .availableForMatching(this.availableForMatching)
                .preferredFestivalType(this.preferredFestivalType)
                .build();
    }
}
