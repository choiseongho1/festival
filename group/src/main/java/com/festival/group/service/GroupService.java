package com.festival.group.service;

import com.festival.group.common.kafka.MatchRequestProducer;
import com.festival.group.domain.Group;
import com.festival.group.dto.GroupSaveDto;
import com.festival.group.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private MatchRequestProducer matchRequestProducer;

  /**
     * 그룹을 생성하고 매치 요청을 Kafka로 전송하는 메서드
     * 
     * @param groupSaveDto 그룹 생성에 필요한 데이터 전송 객체
     * @return 생성된 그룹 객체
     */
    public Group createGroup(GroupSaveDto groupSaveDto) {
        // DTO를 엔티티로 변환하여 그룹 생성
        Group group = groupSaveDto.toEntity();
        // 그룹을 데이터베이스에 저장
        Group savedGroup = groupRepository.save(group);

        // 매치 요청을 Kafka로 전송
        matchRequestProducer.sendMatchRequestEvent(savedGroup);

        // 생성된 그룹 반환
        return savedGroup;
    }
}
