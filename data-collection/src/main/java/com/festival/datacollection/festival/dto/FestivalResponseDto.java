package com.festival.datacollection.festival.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FestivalResponseDto {

    @JacksonXmlProperty(localName = "header")
    private Header header;

    @JacksonXmlProperty(localName = "body")
    private Body body;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Header {
        @JacksonXmlProperty(localName = "resultCode")
        private String resultCode;

        @JacksonXmlProperty(localName = "resultMsg")
        private String resultMsg;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        @JacksonXmlProperty(localName = "items")
        private Items items;

        @JacksonXmlProperty(localName = "numOfRows")
        private int numOfRows;

        @JacksonXmlProperty(localName = "pageNo")
        private int pageNo;

        @JacksonXmlProperty(localName = "totalCount")
        private int totalCount;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items {

        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "item")
        private List<FestivalItem> itemList;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FestivalItem {

        @JacksonXmlProperty(localName = "addr1")
        private String addr1;

        @JacksonXmlProperty(localName = "addr2")
        private String addr2;

        @JacksonXmlProperty(localName = "booktour")
        private String booktour;

        @JacksonXmlProperty(localName = "cat1")
        private String cat1;

        @JacksonXmlProperty(localName = "cat2")
        private String cat2;

        @JacksonXmlProperty(localName = "cat3")
        private String cat3;

        @JacksonXmlProperty(localName = "contentid")
        private String contentid;

        @JacksonXmlProperty(localName = "contenttypeid")
        private String contenttypeid;

        @JacksonXmlProperty(localName = "createdtime")
        private String createdtime;

        @JacksonXmlProperty(localName = "eventstartdate")
        private String eventstartdate;

        @JacksonXmlProperty(localName = "eventenddate")
        private String eventenddate;

        @JacksonXmlProperty(localName = "firstimage")
        private String firstimage;

        @JacksonXmlProperty(localName = "firstimage2")
        private String firstimage2;

        @JacksonXmlProperty(localName = "cpyrhtDivCd")
        private String cpyrhtDivCd;

        @JacksonXmlProperty(localName = "mapx")
        private double mapx;

        @JacksonXmlProperty(localName = "mapy")
        private double mapy;

        @JacksonXmlProperty(localName = "mlevel")
        private int mlevel;

        @JacksonXmlProperty(localName = "modifiedtime")
        private String modifiedtime;

        @JacksonXmlProperty(localName = "areacode")
        private String areacode;

        @JacksonXmlProperty(localName = "sigungucode")
        private String sigungucode;

        @JacksonXmlProperty(localName = "tel")
        private String tel;

        @JacksonXmlProperty(localName = "title")
        private String title;
    }
}