package com.festival.datacollection.festival.dto;

import com.festival.datacollection.festival.domain.Festival;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FestivalListDto {
    private String contentId;
    private String title;
    private String addr1;
    private String eventStartDate;
    private String eventEndDate;
    private String firstImage;

    public static FestivalListDto from(Festival festival) {
        return FestivalListDto.builder()
                .contentId(festival.getContentId())
                .title(festival.getTitle())
                .addr1(festival.getAddr1())
                .eventStartDate(festival.getEventStartDate())
                .eventEndDate(festival.getEventEndDate())
                .firstImage(festival.getFirstImage())
                .build();
    }
} 