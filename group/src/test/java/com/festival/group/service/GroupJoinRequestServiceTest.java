package com.festival.group.service;

import com.festival.group.domain.Group;
import com.festival.group.domain.GroupJoinRequest;
import com.festival.group.enums.JoinRequestStatus;
import com.festival.group.repository.GroupJoinRequestRepository;
import com.festival.group.repository.GroupRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupJoinRequestServiceTest {

    private static final Logger log = LoggerFactory.getLogger(GroupJoinRequestServiceTest.class);

    @Mock
    private GroupJoinRequestRepository joinRequestRepository;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupJoinRequestService groupJoinRequestService;

    @Test
    void createJoinRequest_Success() {
        // given
        Long groupId = 1L;
        String userId = "user123";
        String message = "참가하고 싶습니다.";

        Group group = Group.builder()
                .id(groupId)
                .leaderId("leader123")
                .members(new ArrayList<>())
                .build();

        GroupJoinRequest savedRequest = GroupJoinRequest.builder()
                .groupId(groupId)
                .userId(userId)
                .status(JoinRequestStatus.PENDING)
                .message(message)
                .build();

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(joinRequestRepository.findByGroupIdAndUserId(groupId, userId)).thenReturn(Optional.empty());
        when(joinRequestRepository.save(any(GroupJoinRequest.class))).thenReturn(savedRequest);
        
        log.info("테스트 시작: 정상적인 참가 요청 생성");

        // when
        GroupJoinRequest result = groupJoinRequestService.createJoinRequest(groupId, userId, message);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getGroupId()).isEqualTo(groupId);
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getStatus()).isEqualTo(JoinRequestStatus.PENDING);
        assertThat(result.getMessage()).isEqualTo(message);

        verify(groupRepository).findById(groupId);
        verify(joinRequestRepository).findByGroupIdAndUserId(groupId, userId);
        verify(joinRequestRepository).save(any(GroupJoinRequest.class));
        
        log.info("참가 요청이 성공적으로 생성되었습니다.");
    }

    @Test
    void createJoinRequest_GroupNotFound() {
        // given
        Long groupId = 1L;
        String userId = "user123";
        
        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());
        
        log.info("테스트 시작: 존재하지 않는 그룹에 대한 참가 요청");

        // when & then
        assertThatThrownBy(() -> 
            groupJoinRequestService.createJoinRequest(groupId, userId, "message"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 그룹입니다.");

        verify(groupRepository).findById(groupId);
        verify(joinRequestRepository, never()).save(any());
        
        log.info("예상대로 예외가 발생했습니다.");
    }

    @Test
    void createJoinRequest_DuplicateRequest() {
        // given
        Long groupId = 1L;
        String userId = "user123";

        Group group = Group.builder()
                .id(groupId)
                .leaderId("leader123")
                .members(new ArrayList<>())
                .build();

        GroupJoinRequest existingRequest = GroupJoinRequest.builder()
                .groupId(groupId)
                .userId(userId)
                .status(JoinRequestStatus.PENDING)
                .build();

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(joinRequestRepository.findByGroupIdAndUserId(groupId, userId))
                .thenReturn(Optional.of(existingRequest));
        
        log.info("테스트 시작: 중복된 참가 요청");

        // when & then
        assertThatThrownBy(() -> 
            groupJoinRequestService.createJoinRequest(groupId, userId, "message"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 참가 요청이 존재합니다.");

        verify(joinRequestRepository, never()).save(any());
        
        log.info("예상대로 예외가 발생했습니다.");
    }

    @Test
    void createJoinRequest_AlreadyMember() {
        // given
        Long groupId = 1L;
        String userId = "user123";

        Group group = Group.builder()
                .id(groupId)
                .leaderId("leader123")
                .members(new ArrayList<>())
                .maxMembers(5)
                .build();
        group.addMember(userId);

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(joinRequestRepository.findByGroupIdAndUserId(groupId, userId))
                .thenReturn(Optional.empty());
        
        log.info("테스트 시작: 이미 멤버인 사용자의 참가 요청");

        // when & then
        assertThatThrownBy(() -> 
            groupJoinRequestService.createJoinRequest(groupId, userId, "message"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 그룹의 멤버입니다.");

        verify(joinRequestRepository, never()).save(any());
        
        log.info("예상대로 예외가 발생했습니다.");
    }

    @Test
    void createJoinRequest_LeaderAttempt() {
        // given
        Long groupId = 1L;
        String leaderId = "leader123";

        Group group = Group.builder()
                .id(groupId)
                .leaderId(leaderId)
                .members(new ArrayList<>())
                .build();

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(joinRequestRepository.findByGroupIdAndUserId(groupId, leaderId))
                .thenReturn(Optional.empty());
        
        log.info("테스트 시작: 리더의 참가 요청");

        // when & then
        assertThatThrownBy(() -> 
            groupJoinRequestService.createJoinRequest(groupId, leaderId, "message"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("그룹 리더는 참가 요청을 할 수 없습니다.");

        verify(joinRequestRepository, never()).save(any());
        
        log.info("예상대로 예외가 발생했습니다.");
    }

    @Test
    void getJoinRequests_WithStatus_Success() {
        // given
        Long groupId = 1L;
        String status = "PENDING";
        List<GroupJoinRequest> mockRequests = Arrays.asList(
            GroupJoinRequest.builder()
                .groupId(groupId)
                .status(JoinRequestStatus.PENDING)
                .build()
        );

        when(joinRequestRepository.findByGroupIdAndStatus(groupId, JoinRequestStatus.PENDING))
            .thenReturn(mockRequests);

        log.info("테스트 시작: 상태값이 있는 참가 요청 목록 조회");

        // when
        List<GroupJoinRequest> result = groupJoinRequestService.getJoinRequests(groupId, status);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getStatus()).isEqualTo(JoinRequestStatus.PENDING);
        verify(joinRequestRepository).findByGroupIdAndStatus(groupId, JoinRequestStatus.PENDING);
        
        log.info("참가 요청 목록이 성공적으로 조회되었습니다.");
    }

    @Test
    void getJoinRequests_InvalidStatus_ThrowsException() {
        // given
        Long groupId = 1L;
        String invalidStatus = "INVALID_STATUS";
        
        log.info("테스트 시작: 잘못된 상태값으로 참가 요청 목록 조회");

        // when & then
        assertThatThrownBy(() -> 
            groupJoinRequestService.getJoinRequests(groupId, invalidStatus))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("유효하지 않은 상태값입니다.");
        
        log.info("예상대로 예외가 발생했습니다.");
    }

    @Test
    void acceptJoinRequest_Success() {
        // given
        String requestId = "123456789012345678901234"; // 24자리 16진수 문자열
        Long groupId = 1L;
        String userId = "userId";

        Group group = Group.builder()
            .id(groupId)
            .maxMembers(5)
            .members(new ArrayList<>())
            .build();

        GroupJoinRequest request = GroupJoinRequest.builder()
            .id(new ObjectId(requestId))
            .groupId(groupId)
            .userId(userId)
            .status(JoinRequestStatus.PENDING)
            .build();

        when(joinRequestRepository.findById(new ObjectId(requestId))).thenReturn(Optional.of(request));
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(joinRequestRepository.save(any(GroupJoinRequest.class))).thenReturn(request);
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        log.info("테스트 시작: 참가 요청 수락");

        // when
        GroupJoinRequest result = groupJoinRequestService.acceptJoinRequest(requestId);

        // then
        assertThat(result.getStatus()).isEqualTo(JoinRequestStatus.ACCEPTED);
        assertThat(group.getMembers()).contains(userId);
        verify(groupRepository).save(group);
        verify(joinRequestRepository).save(request);
        
        log.info("참가 요청이 성공적으로 수락되었습니다.");
    }

    @Test
    void acceptJoinRequest_GroupFull_ThrowsException() {
        // given
         String requestId = "123456789012345678901234"; // 24자리 16진수 문자열
        Long groupId = 1L;

        Group group = Group.builder()
            .id(groupId)
            .maxMembers(1)
            .members(Arrays.asList("existingMember"))
            .build();

        GroupJoinRequest request = GroupJoinRequest.builder()
            .id(new ObjectId(requestId))
            .groupId(groupId)
            .status(JoinRequestStatus.PENDING)
            .build();

        when(joinRequestRepository.findById(new ObjectId(requestId))).thenReturn(Optional.of(request));
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

        log.info("테스트 시작: 가득 찬 그룹에 대한 참가 요청 수락");

        // when & then
        assertThatThrownBy(() -> 
            groupJoinRequestService.acceptJoinRequest(requestId))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("그룹의 최대 인원을 초과할 수 없습니다.");
        
        log.info("예상대로 예외가 발생했습니다.");
    }

    @Test
    void rejectJoinRequest_Success() {
        // given
        String requestId = "123456789012345678901234"; // 24자리 16진수 문자열
        GroupJoinRequest request = GroupJoinRequest.builder()
            .id(new ObjectId(requestId))
            .status(JoinRequestStatus.PENDING)
            .build();

        when(joinRequestRepository.findById(new ObjectId(requestId))).thenReturn(Optional.of(request));
        when(joinRequestRepository.save(any(GroupJoinRequest.class))).thenReturn(request);

        log.info("테스트 시작: 참가 요청 거절");

        // when
        GroupJoinRequest result = groupJoinRequestService.rejectJoinRequest(requestId);

        // then
        assertThat(result.getStatus()).isEqualTo(JoinRequestStatus.REJECTED);
        verify(joinRequestRepository).save(request);
        
        log.info("참가 요청이 성공적으로 거절되었습니다.");
    }

    @Test
    void getUserJoinRequests_Success() {
        // given
        String userId = "userId";
        List<GroupJoinRequest> mockRequests = Arrays.asList(
            GroupJoinRequest.builder()
                .userId(userId)
                .build()
        );

        when(joinRequestRepository.findByUserId(userId)).thenReturn(mockRequests);

        log.info("테스트 시작: 사용자의 참가 요청 목록 조회");

        // when
        List<GroupJoinRequest> result = groupJoinRequestService.getUserJoinRequests(userId);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getUserId()).isEqualTo(userId);
        verify(joinRequestRepository).findByUserId(userId);
        
        log.info("사용자의 참가 요청 목록이 성공적으로 조회되었습니다.");
    }
} 