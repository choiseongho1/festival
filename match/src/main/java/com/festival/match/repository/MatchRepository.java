package com.festival.match.repository;


import com.festival.match.domain.Match;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MatchRepository extends MongoRepository<Match, String> {
    List<Match> findByFestivalId(String festivalId);
    List<Match> findByLeaderId(String leaderId);
}