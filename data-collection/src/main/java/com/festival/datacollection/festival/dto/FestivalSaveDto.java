package com.festival.datacollection.festival.dto;

import com.festival.datacollection.festival.domain.Festival;

import lombok.*;

@Getter
@Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class FestivalSaveDto {

    private String addr1;
    private String addr2;
    private String contentid;
    private String eventstartdate;
    private String eventenddate;
    private String firstimage;
    private String firstimage2;
    private String tel;
    private String title;
    private Double mapx;
    private Double mapy;

    public Festival toEntity() {
        return Festival.builder()
                .addr1(addr1)
                .addr2(addr2)
                .contentId(contentid)
                .eventStartDate(eventstartdate)
                .eventEndDate(eventenddate)
                .firstImage(firstimage)
                .firstImage2(firstimage2)
                .tel(tel)
                .title(title)
                .mapx(mapx)
                .mapy(mapy)
                .build();
    }
}