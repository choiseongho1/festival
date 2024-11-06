package com.festival.datacollection.festival.dto;

import com.festival.datacollection.festival.domain.Festival;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FestivalDetailDto {
    private String contentId;
    private String title;
    private String addr1;
    private String addr2;
    private String eventStartDate;
    private String eventEndDate;
    private String firstImage;
    private String firstImage2;
    private Double mapx;
    private Double mapy;
    private String tel;

    public static FestivalDetailDto from(Festival festival) {
        return FestivalDetailDto.builder()
                .contentId(festival.getContentId())
                .title(festival.getTitle())
                .addr1(festival.getAddr1())
                .addr2(festival.getAddr2())
                .eventStartDate(festival.getEventStartDate())
                .eventEndDate(festival.getEventEndDate())
                .firstImage(festival.getFirstImage())
                .firstImage2(festival.getFirstImage2())
                .mapx(festival.getMapx())
                .mapy(festival.getMapy())
                .tel(festival.getTel())
                .build();
    }
} 