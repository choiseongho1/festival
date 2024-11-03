package com.festival.group.service;

import com.festival.group.domain.Group;
import com.festival.group.domain.GroupJoinRequest;
import com.festival.group.enums.JoinRequestStatus;
import com.festival.group.repository.GroupJoinRequestRepository;
import com.festival.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.bson.types.ObjectId;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupJoinRequestService {

    private final GroupJoinRequestRepository joinRequestRepository;
    private final GroupRepository groupRepository;

    /**
     * 그룹 참가 요청을 생성합니다.
     */
    public GroupJoinRequest createJoinRequest(Long groupId, String userId, String message) {
        log.info("그룹 참가 요청 시작. GroupId: {}, UserId: {}", groupId, userId);

        // 1. 그룹 존재 여부 확인
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));

        // 2. 이미 참가 요청이 있는지 확인
        joinRequestRepository.findByGroupIdAndUserId(groupId, userId)
                .ifPresent(request -> {
                    throw new IllegalStateException("이미 참가 요청이 존재합니다.");
                });

        // 3. 이미 그룹 멤버인지 확인
        if (group.getMembers() != null && group.getMembers().contains(userId)) {
            throw new IllegalStateException("이미 그룹의 멤버입니다.");
        }

        // 4. 리더는 참가 요청할 수 없음
        if (group.getLeaderId().equals(userId)) {
            throw new IllegalStateException("그룹 리더는 참가 요청을 할 수 없습니다.");
        }

        try {
            // 5. 참가 요청 생성
            GroupJoinRequest joinRequest = GroupJoinRequest.builder()
                    .groupId(groupId)
                    .userId(userId)
                    .status(JoinRequestStatus.PENDING)
                    .message(message)
                    .build();

            // 6. 저장
            GroupJoinRequest savedRequest = joinRequestRepository.save(joinRequest);
            log.info("그룹 참가 요청 생성 완료. RequestId: {}", savedRequest.getId());

            return savedRequest;

        } catch (Exception e) {
            log.error("그룹 참가 요청 생성 중 오류 발생. Error: {}", e.getMessage());
            throw new RuntimeException("그룹 참가 요청 생성 실패", e);
        }
    }

    /**
     * 그룹의 참가 요청 목록을 조회합니다.
     */
    public List<GroupJoinRequest> getJoinRequests(Long groupId, String status) {
        log.info("그룹 참가 요청 목록 조회. GroupId: {}, Status: {}", groupId, status);
        
        if (status != null) {
            try {
                JoinRequestStatus requestStatus = JoinRequestStatus.valueOf(status.toUpperCase());
                return joinRequestRepository.findByGroupIdAndStatus(groupId, requestStatus);
            } catch (IllegalArgumentException e) {
                log.error("잘못된 상태값입니다. Status: {}", status);
                throw new IllegalArgumentException("유효하지 않은 상태값입니다.");
            }
        }
        
        return joinRequestRepository.findByGroupId(groupId);
    }

    /**
     * 참가 요청을 수락합니다.
     */
    public GroupJoinRequest acceptJoinRequest(String requestId) {
        log.info("참가 요청 수락 시작. RequestId: {}", requestId);

        GroupJoinRequest request = getAndValidateRequest(requestId);
        Group group = getAndValidateGroup(request.getGroupId());

        // 그룹 수용 가능 인원 확인
        if (!group.canAcceptMoreMembers()) {
            throw new IllegalStateException("그룹의 최대 인원을 초과할 수 없습니다.");
        }

        try {
            // 그룹에 멤버 추가
            group.addMember(request.getUserId());
            groupRepository.save(group);

            // 요청 상태 업데이트
            request.setStatus(JoinRequestStatus.ACCEPTED);
            GroupJoinRequest updatedRequest = joinRequestRepository.save(request);
            
            log.info("참가 요청 수락 완료. RequestId: {}, UserId: {}", requestId, request.getUserId());
            return updatedRequest;
            
        } catch (Exception e) {
            log.error("참가 요청 수락 중 오류 발생. RequestId: {}, Error: {}", requestId, e.getMessage());
            throw new RuntimeException("참가 요청 수락 실패", e);
        }
    }

    /**
     * 참가 요청을 거절합니다.
     */
    public GroupJoinRequest rejectJoinRequest(String requestId) {
        log.info("참가 요청 거절 시작. RequestId: {}", requestId);

        GroupJoinRequest request = getAndValidateRequest(requestId);

        try {
            request.setStatus(JoinRequestStatus.REJECTED);
            GroupJoinRequest updatedRequest = joinRequestRepository.save(request);
            
            log.info("참가 요청 거절 완료. RequestId: {}, UserId: {}", requestId, request.getUserId());
            return updatedRequest;
            
        } catch (Exception e) {
            log.error("참가 요청 거절 중 오류 발생. RequestId: {}, Error: {}", requestId, e.getMessage());
            throw new RuntimeException("참가 요청 거절 실패", e);
        }
    }

    /**
     * 사용자의 참가 요청 목록을 조회합니다.
     */
    public List<GroupJoinRequest> getUserJoinRequests(String userId) {
        log.info("사용자의 참가 요청 목록 조회. UserId: {}", userId);
        return joinRequestRepository.findByUserId(userId);
    }

    /**
     * 참가 요청을 조회하고 유효성을 검증합니다.
     */
    private GroupJoinRequest getAndValidateRequest(String requestId) {
        GroupJoinRequest request = joinRequestRepository.findById(new ObjectId(requestId))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 참가 요청입니다."));

        if (request.getStatus() != JoinRequestStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 참가 요청입니다.");
        }

        return request;
    }

    /**
     * 그룹을 조회하고 유효성을 검증합니다.
     */
    private Group getAndValidateGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));
    }
} 