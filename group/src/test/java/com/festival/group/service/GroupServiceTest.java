package com.festival.group.service;

import com.festival.group.common.kafka.MatchRequestProducer;
import com.festival.group.domain.Group;
import com.festival.group.dto.GroupSaveDto;
import com.festival.group.repository.GroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    private static final Logger log = LoggerFactory.getLogger(GroupServiceTest.class);

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private MatchRequestProducer matchRequestProducer;

    @InjectMocks
    private GroupService groupService;

    @Test
    void createGroup_ValidInput_Success() {
        // given
        // 테스트용 그룹 정보 설정
        GroupSaveDto groupSaveDto = GroupSaveDto.builder()
                .groupName("테스트 그룹")
                .maxMembers(4)
                .festivalId("0")
                .build();

        // 저장될 그룹 엔티티 생성
        Group group = groupSaveDto.toEntity();
        Group savedGroup = Group.builder()
                .id(1L)
                .groupName("테스트 그룹")
                .maxMembers(4)
                .festivalId("0")
                .build();

        // groupRepository.save() 호출 시 savedGroup 반환하도록 설정
        when(groupRepository.save(any(Group.class))).thenReturn(savedGroup);
        
        // Kafka 프로듀서 동작 설정 (void 메서드이므로 doNothing 사용)
        doNothing().when(matchRequestProducer).sendMatchRequestEvent(any(Group.class));
        
        log.info("테스트 시작: 유효한 입력으로 그룹 생성 시도");

        // when
        Group result = groupService.createGroup(groupSaveDto);

        // then
        log.info("생성된 그룹 정보: {}", result);
        
        // 그룹이 정상적으로 생성되었는지 검증
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getGroupName()).isEqualTo("테스트 그룹");
        assertThat(result.getMaxMembers()).isEqualTo(4);

        // Repository와 Producer가 정상적으로 호출되었는지 검증
        verify(groupRepository).save(any(Group.class));
        verify(matchRequestProducer).sendMatchRequestEvent(any(Group.class));
        
        log.info("그룹 생성 및 매치 요청 이벤트 발행 완료");
    }
}