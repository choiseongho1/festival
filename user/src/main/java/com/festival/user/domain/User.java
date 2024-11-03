package com.festival.user.domain;

import com.festival.user.common.domain.BaseDomain;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Document(collection = "users")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class User extends BaseDomain {

    @Id
    private String id;  // MongoDB에서 자동 생성되는 고유 ID

    @Indexed(unique = true)
    private String username;  // 사용자 아이디 또는 이메일 (중복 불가)

    private String password;  // 비밀번호 (암호화 필요)

    private String fullName;  // 사용자 이름
    private String phoneNumber;  // 연락처 (필수)
    private boolean phoneVerified;  // 전화번호 인증 여부

    private String favoriteFestival;  // 선호하는 축제 (사용자 프로필용)

    // 추가로 매칭 시스템에 필요한 정보들
    private boolean availableForMatching;  // 매칭 가능 여부
    private String preferredFestivalType;  // 선호하는 축제 종류 (음악, 문화 등)

}
