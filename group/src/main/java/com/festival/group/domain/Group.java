package com.festival.group.domain;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "groups")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Group {

    @Id
    private Long id;

    private String groupName;            // 그룹 이름
    private String leaderId;             // 그룹 리더 ID
    private String festivalId;           // 축제 ID
    private int maxMembers;              // 그룹의 최대 인원수

    @Builder.Default
    private List<String> members = new ArrayList<>();    // 그룹 멤버 ID 목록

    /**
     * 새로운 멤버를 추가합니다.
     * @throws IllegalStateException 그룹이 이미 가득 찬 경우
     */
    public void addMember(String memberId) {
        if (members == null) {
            members = new ArrayList<>();
        }
        
        if (members.size() >= maxMembers) {
            throw new IllegalStateException("그룹이 이미 가득 찼습니다.");
        }
        
        if (!members.contains(memberId)) {
            members.add(memberId);
        }
    }

    /**
     * 멤버를 제거합니다.
     */
    public void removeMember(String memberId) {
        if (members != null) {
            members.remove(memberId);
        }
    }

    /**
     * 현재 그룹의 멤버 수를 반환합니다.
     */
    public int getCurrentMemberCount() {
        return members != null ? members.size() : 0;
    }

    /**
     * 그룹에 더 많은 멤버를 받을 수 있는지 확인합니다.
     */
    public boolean canAcceptMoreMembers() {
        return getCurrentMemberCount() < maxMembers;
    }
}