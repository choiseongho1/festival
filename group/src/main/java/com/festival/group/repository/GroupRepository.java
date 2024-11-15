package com.festival.group.repository;

import com.festival.group.domain.Group;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends MongoRepository<Group, Long> {
    List<Group> findByFestivalId(String festivalId);
    Optional<Group> findByLeaderId(String leaderId);
    List<Group> findByMembersContaining(String memberId);
}