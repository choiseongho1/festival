package com.festival.match.domain;

import com.festival.match.common.domain.BaseDomain;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "matches")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Match extends BaseDomain {

    @Id
    private String id;
    private String leaderId;            // 그룹 리더 ID
    private List<String> memberIds;     // 그룹 참여자 ID 목록
    private String festivalId;          // 축제 ID
    private int score;                  // 매칭 점수
    private String status;              // 매칭 상태
}