package com.festival.group.repository;

import com.festival.group.domain.GroupJoinRequest;
import com.festival.group.enums.JoinRequestStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupJoinRequestRepository extends MongoRepository<GroupJoinRequest, ObjectId> {
    Optional<GroupJoinRequest> findByGroupIdAndUserId(Long groupId, String userId);
    List<GroupJoinRequest> findByGroupIdAndStatus(Long groupId, JoinRequestStatus status);
    List<GroupJoinRequest> findByUserId(String userId);

    List<GroupJoinRequest> findByGroupId(Long groupId);
}