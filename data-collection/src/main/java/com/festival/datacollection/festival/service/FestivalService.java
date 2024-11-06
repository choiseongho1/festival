package com.festival.datacollection.festival.service;

import com.festival.datacollection.festival.domain.Festival;
import com.festival.datacollection.festival.dto.FestivalDetailDto;
import com.festival.datacollection.festival.dto.FestivalListDto;
import com.festival.datacollection.festival.repository.FestivalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FestivalService {

    private final FestivalRepository festivalRepository;

    // 축제 목록 조회 (페이징)
    public Page<FestivalListDto> getFestivalList(Pageable pageable) {
        log.info("축제 목록 조회 요청. page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return festivalRepository.findCurrentAndUpcomingFestivals(pageable)
                .map(FestivalListDto::from);
    }

    // 축제 상세 조회
    public FestivalDetailDto getFestivalDetail(String contentId) {
        log.info("축제 상세 정보 조회 요청. contentId: {}", contentId);
        Festival festival = festivalRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 축제를 찾을 수 없습니다. contentId: " + contentId));
        return FestivalDetailDto.from(festival);
    }

    // 축제 검색 (제목)
    public Page<FestivalListDto> searchFestivalsByTitle(String keyword, Pageable pageable) {
        log.info("축제 제목 검색 요청. keyword: {}", keyword);
        return festivalRepository.findByTitleContaining(keyword, pageable)
                .map(FestivalListDto::from);
    }

    // 축제 검색 (지역)
    public Page<FestivalListDto> searchFestivalsByLocation(String location, Pageable pageable) {
        log.info("축제 지역 검색 요청. location: {}", location);
        return festivalRepository.findByAddr1Containing(location, pageable)
                .map(FestivalListDto::from);
    }
} 