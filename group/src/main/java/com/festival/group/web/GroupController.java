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

@RestController
@RequestMapping("/api/group")
@Slf4j
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final GroupJoinRequestService joinRequestService;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Group Server is running successfully";
    }

    @PostMapping("/create")
    public ResponseEntity<Group> createGroup(@RequestBody GroupSaveDto groupSaveDto) {
        Group group = groupService.createGroup(groupSaveDto);
        return ResponseEntity.ok(group);
    }

    /**
     * 그룹 참가 요청을 생성합니다.
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
     * 그룹의 참가 요청 목록을 조회합니다.
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
     * 사용자의 참가 요청 목록을 조회합니다.
     */
    @GetMapping("/join-requests/user/{userId}")
    public ResponseEntity<List<GroupJoinRequest>> getUserJoinRequests(
            @PathVariable String userId) {
        
        log.info("사용자의 참가 요청 목록 조회. UserId: {}", userId);
        
        List<GroupJoinRequest> requests = joinRequestService.getUserJoinRequests(userId);
        return ResponseEntity.ok(requests);
    }

    /**
     * 참가 요청을 수락합니다.
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
     * 참가 요청을 거절합니다.
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
