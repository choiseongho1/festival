package com.festival.group.service;

import com.festival.group.common.kafka.MatchRequestProducer;
import com.festival.group.domain.Group;
import com.festival.group.dto.GroupSaveDto;
import com.festival.group.repository.GroupRepository;
import com.festival.group.client.UserServiceClient;
import org.hibernate.service.spi.ServiceException;
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

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private GroupService groupService;

    @Test
    void createGroup_VerifiedUser_Success() {
        // given
        String leaderId = "user123";
        GroupSaveDto groupSaveDto = GroupSaveDto.builder()
                .leaderId(leaderId)
                .groupName("테스트 그룹")
                .maxMembers(4)
                .build();

        Group savedGroup = Group.builder()
                .id(1L)
                .leaderId(leaderId)
                .groupName("테스트 그룹")
                .maxMembers(4)
                .build();

        when(userServiceClient.isPhoneVerified(leaderId)).thenReturn(true);
        when(groupRepository.save(any(Group.class))).thenReturn(savedGroup);
        
        log.info("테스트 시작: 휴대폰 인증된 사용자의 그룹 생성");

        // when
        Group result = groupService.createGroup(groupSaveDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getLeaderId()).isEqualTo(leaderId);
        verify(groupRepository).save(any(Group.class));
        verify(matchRequestProducer).sendMatchRequestEvent(any(Group.class));
        
        log.info("그룹이 성공적으로 생성되었습니다. GroupId: {}", result.getId());
    }

    @Test
    void createGroup_UnverifiedUser_ThrowsException() {
        // given
        String leaderId = "user123";
        GroupSaveDto groupSaveDto = GroupSaveDto.builder()
                .leaderId(leaderId)
                .groupName("테스트 그룹")
                .maxMembers(4)
                .build();

        when(userServiceClient.isPhoneVerified(leaderId)).thenReturn(false);
        
        log.info("테스트 시작: 휴대폰 미인증 사용자의 그룹 생성 시도");

        // when & then
        assertThatThrownBy(() -> groupService.createGroup(groupSaveDto))
                .isInstanceOf(ServiceException.class)
                .hasMessage("그룹 생성을 위해서는 휴대폰 인증이 필요합니다.");

        verify(groupRepository, never()).save(any());
        verify(matchRequestProducer, never()).sendMatchRequestEvent(any());
        
        log.info("예상대로 인증 예외가 발생했습니다.");
    }

    @Test
    void createGroup_EmptyLeaderId_ThrowsException() {
        // given
        GroupSaveDto groupSaveDto = GroupSaveDto.builder()
                .groupName("테스트 그룹")
                .maxMembers(4)
                .build();
        
        log.info("테스트 시작: 리더 ID가 없는 그룹 생성 시도");

        // when & then
        assertThatThrownBy(() -> groupService.createGroup(groupSaveDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹장 ID는 필수입니다.");
        
        log.info("예상대로 유효성 검사 예외가 발생했습니다.");
    }
}