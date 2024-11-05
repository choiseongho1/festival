package com.festival.group.web;

import com.festival.group.domain.Group;
import com.festival.group.domain.GroupJoinRequest;
import com.festival.group.dto.GroupSaveDto;
import com.festival.group.dto.CreateJoinRequestDto;
import com.festival.group.dto.UpdateRequestStatusDto;
import com.festival.group.service.GroupService;
import com.festival.group.service.GroupJoinRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 축제 그룹 관련 요청을 처리하는 컨트롤러
 * 그룹 생성, 참가 요청 관리 등의 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/group")
@Slf4j
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final GroupJoinRequestService joinRequestService;

    /**
     * 서버 상태를 확인하는 헬스 체크 엔드포인트
     * @return 서버 상태 메시지
     */
    @GetMapping("/health-check")
    public String healthCheck() {
        return "Group Server is running successfully";
    }

    /**
     * 새로운 축제 그룹을 생성합니다.
     * 
     * @param groupSaveDto 그룹 생성에 필요한 정보 (축제ID, 최대인원, 설명 등)
     * @return 생성된 그룹 정보
     */
    @PostMapping("/create")
    public ResponseEntity<Group> createGroup(@RequestBody GroupSaveDto groupSaveDto) {
        Group group = groupService.createGroup(groupSaveDto);
        return ResponseEntity.ok(group);
    }

    /**
     * 특정 그룹에 대한 참가 요청을 생성합니다.
     * 
     * @param groupId 참가를 원하는 그룹의 ID
     * @param request 참가 요청 정보 (사용자ID, 참가 메시지)
     * @return 생성된 참가 요청 정보
     */
    @PostMapping("/{groupId}/join")
    public ResponseEntity<GroupJoinRequest> requestToJoin(
            @PathVariable Long groupId,
            @RequestBody CreateJoinRequestDto request) {
        
        log.info("그룹 참가 요청 수신. GroupId: {}, UserId: {}", 
                groupId, request.getUserId());

        GroupJoinRequest joinRequest = joinRequestService.createJoinRequest(
                groupId,
                request.getUserId(),
                request.getMessage()
        );

        log.info("그룹 참가 요청 생성 완료. RequestId: {}", joinRequest.getId());
        return ResponseEntity.ok(joinRequest);
    }

    /**
     * 특정 그룹의 모든 참가 요청 목록을 조회합니다.
     * 선택적으로 상태(PENDING, ACCEPTED, REJECTED)를 기준으로 필터링할 수 있습니다.
     * 
     * @param groupId 조회할 그룹의 ID
     * @param status 필터링할 참가 요청 상태 (선택사항)
     * @return 참가 요청 목록
     */
    @GetMapping("/{groupId}/join-requests")
    public ResponseEntity<List<GroupJoinRequest>> getGroupJoinRequests(
            @PathVariable Long groupId,
            @RequestParam(required = false) String status) {
        
        log.info("그룹 참가 요청 목록 조회. GroupId: {}, Status: {}", groupId, status);
        
        List<GroupJoinRequest> requests = joinRequestService.getJoinRequests(groupId, status);
        return ResponseEntity.ok(requests);
    }

    /**
     * 특정 사용자의 모든 그룹 참가 요청 목록을 조회합니다.
     * 사용자가 신청한 모든 참가 요청의 이력을 확인할 수 있습니다.
     * 
     * @param userId 조회할 사용자의 ID
     * @return 해당 사용자의 참가 요청 목록
     */
    @GetMapping("/join-requests/user/{userId}")
    public ResponseEntity<List<GroupJoinRequest>> getUserJoinRequests(
            @PathVariable String userId) {
        
        log.info("사용자의 참가 요청 목록 조회. UserId: {}", userId);
        
        List<GroupJoinRequest> requests = joinRequestService.getUserJoinRequests(userId);
        return ResponseEntity.ok(requests);
    }
 
    /**
     * 특정 참가 요청을 수락합니다.
     * 그룹의 리더만이 이 작업을 수행할 수 있습니다.
     * 요청이 수락되면 해당 사용자는 그룹의 멤버가 됩니다.
     * 
     * @param requestId 수락할 참가 요청의 ID
     * @return 수정된 참가 요청 정보
     */
    @PutMapping("/join-requests/{requestId}/accept")
    public ResponseEntity<GroupJoinRequest> acceptJoinRequest(
            @PathVariable String requestId) {
        
        log.info("참가 요청 수락. RequestId: {}", requestId);
        
        GroupJoinRequest updatedRequest = joinRequestService.acceptJoinRequest(requestId);
        
        log.info("참가 요청 수락 완료. RequestId: {}", requestId);
        return ResponseEntity.ok(updatedRequest);
    }

    /**
     * 특정 참가 요청을 거절합니다.
     * 그룹의 리더만이 이 작업을 수행할 수 있습니다.
     * 요청이 거절되면 해당 사용자는 그룹에 참가할 수 없습니다.
     * 
     * @param requestId 거절할 참가 요청의 ID
     * @return 수정된 참가 요청 정보
     */
    @PutMapping("/join-requests/{requestId}/reject")
    public ResponseEntity<GroupJoinRequest> rejectJoinRequest(
            @PathVariable String requestId) {
        
        log.info("참가 요청 거절. RequestId: {}", requestId);
        
        GroupJoinRequest updatedRequest = joinRequestService.rejectJoinRequest(requestId);
        
        log.info("참가 요청 거절 완료. RequestId: {}", requestId);
        return ResponseEntity.ok(updatedRequest);
    }
}
