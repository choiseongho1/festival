package com.festival.datacollection.festival.web;

import com.festival.datacollection.festival.dto.FestivalDetailDto;
import com.festival.datacollection.festival.dto.FestivalListDto;
import com.festival.datacollection.festival.service.FestivalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/data-collection")
@RequiredArgsConstructor
public class FestivalController {

    private final FestivalService festivalService;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Data-collection Server is running successfully";
    }

    @GetMapping
    public ResponseEntity<Page<FestivalListDto>> getFestivalList(
            @PageableDefault(size = 9) Pageable pageable) {
        return ResponseEntity.ok(festivalService.getFestivalList(pageable));
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<FestivalDetailDto> getFestivalDetail(
            @PathVariable String contentId) {
        return ResponseEntity.ok(festivalService.getFestivalDetail(contentId));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<FestivalListDto>> searchFestivals(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @PageableDefault(size = 10) Pageable pageable) {
        
        if (title != null && !title.isEmpty()) {
            return ResponseEntity.ok(festivalService.searchFestivalsByTitle(title, pageable));
        } else if (location != null && !location.isEmpty()) {
            return ResponseEntity.ok(festivalService.searchFestivalsByLocation(location, pageable));
        }
        
        return ResponseEntity.ok(festivalService.getFestivalList(pageable));
    }
}
