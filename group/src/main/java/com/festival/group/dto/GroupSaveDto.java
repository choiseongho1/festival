package com.festival.group.dto;

import com.festival.group.domain.Group;
import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class GroupSaveDto {

    private String groupName;            // 그룹 이름
    private String leaderId;             // 그룹 리더 ID
    private String festivalId;           // 축제 ID
    private int maxMembers;              // 그룹의 최대 인원수

    public Group toEntity(){
        return Group.builder()
                .groupName(this.groupName)
                .leaderId(this.leaderId)
                .festivalId(this.festivalId)
                .maxMembers(this.maxMembers)
                .build();
    }
}
