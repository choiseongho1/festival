package com.festival.group.domain;


import com.festival.group.common.domain.BaseDomain;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "groups")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Group extends BaseDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupName;            // 그룹 이름
    private String leaderId;             // 그룹 리더 ID
    private String festivalId;           // 축제 ID
    private int maxMembers;              // 그룹의 최대 인원수

}