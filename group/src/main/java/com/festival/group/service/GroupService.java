package com.festival.group.service;

import com.festival.group.domain.Group;
import com.festival.group.dto.GroupSaveDto;
import com.festival.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    /**
     * 새로운 그룹을 생성합니다.
     */
    public Group createGroup(GroupSaveDto createDto) {
        log.info("그룹 생성 시작. LeaderId: {}, FestivalId: {}", createDto.getLeaderId(), createDto.getFestivalId());

        // 동일한 축제에 대해 이미 그룹을 생성했는지 확인
        groupRepository.findByLeaderId(createDto.getLeaderId())
                .ifPresent(existingGroup -> {
                    if (existingGroup.getFestivalId().equals(createDto.getFestivalId())) {
                        throw new IllegalStateException("이미 해당 축제에 대한 그룹을 생성했습니다.");
                    }
                });

        try {
            Group group = Group.builder()
                    .groupName(createDto.getGroupName())
                    .leaderId(createDto.getLeaderId())
                    .festivalId(createDto.getFestivalId())
                    .maxMembers(createDto.getMaxMembers())
                    .build();

            // 리더를 멤버로 자동 추가
            group.addMember(createDto.getLeaderId());

            Group savedGroup = groupRepository.save(group);
            log.info("그룹 생성 완료. GroupId: {}", savedGroup.getId());
            
            return savedGroup;

        } catch (Exception e) {
            log.error("그룹 생성 중 오류 발생. Error: {}", e.getMessage());
            throw new RuntimeException("그룹 생성 실패", e);
        }
    }

    /**
     * 그룹 정보를 수정합니다.
     */
    public Group updateGroup(Long groupId, GroupSaveDto updateDto) {
        log.info("그룹 정보 수정 시작. GroupId: {}", groupId);

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));

        // 리더만 수정 가능
        if (!group.getLeaderId().equals(updateDto.getLeaderId())) {
            throw new IllegalStateException("그룹 리더만 정보를 수정할 수 있습니다.");
        }

        try {
            group.setGroupName(updateDto.getGroupName());
            group.setMaxMembers(updateDto.getMaxMembers());

            Group updatedGroup = groupRepository.save(group);
            log.info("그룹 정보 수정 완료. GroupId: {}", groupId);
            
            return updatedGroup;

        } catch (Exception e) {
            log.error("그룹 정보 수정 중 오류 발생. GroupId: {}, Error: {}", groupId, e.getMessage());
            throw new RuntimeException("그룹 정보 수정 실패", e);
        }
    }

    /**
     * 그룹을 삭제합니다.
     */
    public void deleteGroup(Long groupId, String userId) {
        log.info("그룹 삭제 시작. GroupId: {}, UserId: {}", groupId, userId);

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));

        // 리더만 삭제 가능
        if (!group.getLeaderId().equals(userId)) {
            throw new IllegalStateException("그룹 리더만 그룹을 삭제할 수 있습니다.");
        }

        try {
            groupRepository.delete(group);
            log.info("그룹 삭제 완료. GroupId: {}", groupId);

        } catch (Exception e) {
            log.error("그룹 삭제 중 오류 발생. GroupId: {}, Error: {}", groupId, e.getMessage());
            throw new RuntimeException("그룹 삭제 실패", e);
        }
    }

    /**
     * 특정 그룹의 정보를 조회합니다.
     */
    public Group getGroup(Long groupId) {
        log.info("그룹 정보 조회. GroupId: {}", groupId);
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));
    }

    /**
     * 특정 축제의 모든 그룹을 조회합니다.
     */
    public List<Group> getGroupsByFestival(String festivalId) {
        log.info("축제별 그룹 목록 조회. FestivalId: {}", festivalId);
        return groupRepository.findByFestivalId(festivalId);
    }

    /**
     * 사용자가 속한 모든 그룹을 조회합니다.
     */
    public List<Group> getUserGroups(String userId) {
        log.info("사용자의 그룹 목록 조회. UserId: {}", userId);
        return groupRepository.findByMembersContaining(userId);
    }

    /**
     * 사용자가 리더인 그룹을 조회합니다.
     */
    public Group getLeaderGroup(String userId) {
        log.info("리더의 그룹 조회. UserId: {}", userId);
        return groupRepository.findByLeaderId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 리더인 그룹이 없습니다."));
    }
}
