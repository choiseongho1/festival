package com.festival.datacollection.festival.repository;

import com.festival.datacollection.festival.domain.Festival;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FestivalRepository extends JpaRepository<Festival, String> {
    
    // 현재 진행 중이거나 예정된 축제 조회
    @Query("SELECT f FROM Festival f WHERE f.eventEndDate >= CURRENT_DATE ORDER BY f.eventStartDate ASC")
    Page<Festival> findCurrentAndUpcomingFestivals(Pageable pageable);
    
    // 제목으로 검색
    Page<Festival> findByTitleContaining(String keyword, Pageable pageable);
    
    // 지역으로 검색
    Page<Festival> findByAddr1Containing(String location, Pageable pageable);
}
