package com.festival.group.service;

import com.festival.group.client.UserServiceClient;
import com.festival.group.common.kafka.MatchRequestProducer;
import com.festival.group.domain.Group;
import com.festival.group.dto.GroupSaveDto;
import com.festival.group.repository.GroupRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final MatchRequestProducer matchRequestProducer;
    private final UserServiceClient userServiceClient;

    /**
     * 그룹 생성 메서드
     * leaderId로 휴대폰 인증 여부를 확인하고 그룹을 생성합니다.
     */
    public Group createGroup(GroupSaveDto groupSaveDto) {
        String leaderId = groupSaveDto.getLeaderId();
        
        // leaderId 유효성 검사
        if (StringUtils.isEmpty(leaderId)) {
            throw new IllegalArgumentException("그룹장 ID는 필수입니다.");
        }

        // 휴대폰 인증 여부 확인
        try {
            boolean isVerified = userServiceClient.isPhoneVerified(leaderId);
            if (!isVerified) {
                log.error("휴대폰 미인증 사용자의 그룹 생성 시도. LeaderId: {}", leaderId);
                throw new ServiceException("그룹 생성을 위해서는 휴대폰 인증이 필요합니다.");
            }
        } catch (FeignException e) {
            log.error("사용자 서비스 호출 중 오류 발생. LeaderId: {}, Error: {}", leaderId, e.getMessage());
            throw new ServiceException("사용자 인증 상태 확인 중 오류가 발생했습니다.");
        }

        log.info("그룹 생성 시작. LeaderId: {}", leaderId);

        try {
            // 그룹 생성 및 저장
            Group group = groupSaveDto.toEntity();
            Group savedGroup = groupRepository.save(group);
            log.info("그룹 생성 완료. GroupId: {}, LeaderId: {}", 
                    savedGroup.getId(), savedGroup.getLeaderId());

            // 매치 요청 이벤트 발행 (비동기)
            matchRequestProducer.sendMatchRequestEvent(savedGroup);
            log.info("매치 요청 이벤트 전송 완료. GroupId: {}", savedGroup.getId());

            return savedGroup;
        } catch (Exception e) {
            log.error("그룹 생성 중 오류 발생. LeaderId: {}, Error: {}", leaderId, e.getMessage());
            throw new RuntimeException("그룹 생성 실패", e);
        }
    }
}
