package com.festival.group.domain;

import com.festival.group.common.domain.BaseDomain;
import com.festival.group.enums.JoinRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "group_join_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupJoinRequest extends BaseDomain {
    
    @Id
    private ObjectId id;            // MongoDB ObjectId
    
    private Long groupId;           // 참가 요청한 그룹 ID
    private String userId;          // 요청한 사용자 ID
    private JoinRequestStatus status; // 요청 상태
    private String message;         // 참가 요청 메시지 (선택)

    // ObjectId를 String으로 변환하는 편의 메서드
    public String getStringId() {
        return id != null ? id.toString() : null;
    }
}
