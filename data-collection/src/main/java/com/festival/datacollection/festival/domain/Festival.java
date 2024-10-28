package com.festival.datacollection.festival.domain;


import com.festival.datacollection.common.domain.BaseDomain;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "festival")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Festival extends BaseDomain {

    @Id
    @Column(name = "content_id", nullable = false, columnDefinition = "VARCHAR(50) COMMENT '콘텐츠 ID'")
    private String contentId;

    @Column(name = "title", nullable = false, length = 255, columnDefinition = "VARCHAR(255) COMMENT '축제 제목'")
    private String title;

    @Column(name = "addr1", nullable = false, length = 255, columnDefinition = "VARCHAR(255) COMMENT '축제 위치 주소1'")
    private String addr1;

    @Column(name = "addr2", length = 255, columnDefinition = "VARCHAR(255) COMMENT '축제 위치 주소2'")
    private String addr2;

    @Column(name = "event_start_date", nullable = false, columnDefinition = "DATE COMMENT '축제 시작 날짜'")
    private String eventStartDate;

    @Column(name = "event_end_date", nullable = false, columnDefinition = "DATE COMMENT '축제 종료 날짜'")
    private String eventEndDate;

    @Column(name = "first_image", length = 500, columnDefinition = "VARCHAR(500) COMMENT '대표 이미지 URL'")
    private String firstImage;

    @Column(name = "first_image2", length = 500, columnDefinition = "VARCHAR(500) COMMENT '두 번째 대표 이미지 URL'")
    private String firstImage2;

    @Column(name = "mapx", columnDefinition = "DECIMAL(15, 10) COMMENT '축제 장소의 경도'")
    private Double mapx;

    @Column(name = "mapy", columnDefinition = "DECIMAL(15, 10) COMMENT '축제 장소의 위도'")
    private Double mapy;

    @Column(name = "tel", length = 100, columnDefinition = "VARCHAR(100) COMMENT '연락처'")
    private String tel;

    @Column(name = "modified_time", columnDefinition = "VARCHAR(50) COMMENT '수정 시간'")
    private String modifiedTime;
}
