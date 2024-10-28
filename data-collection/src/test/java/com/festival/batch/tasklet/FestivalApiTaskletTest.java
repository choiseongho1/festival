package com.festival.batch.tasklet;

import com.festival.datacollection.festival.domain.Festival;
import com.festival.datacollection.festival.repository.FestivalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FestivalApiTaskletTest {

    @Mock
    private FestivalRepository festivalRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FestivalApiTasklet festivalApiTasklet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchFestivalDataAsync() throws Exception {
        // given
        String eventStartDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // Mock API Response
        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, Object> responseBody = new HashMap<>();
        Map<String, Object> items = new HashMap<>();
        Map<String, Object> item = new HashMap<>();
        item.put("addr1", "서울특별시 송파구 양재대로 932 (가락동)");
        item.put("addr2", "가락몰");
        item.put("contentid", "3113671");
        item.put("eventstartdate", "20240510");
        item.put("eventenddate", "20240512");
        item.put("firstimage", "http://example.com/image1.jpg");
        item.put("firstimage2", "http://example.com/image2.jpg");
        item.put("tel", "02-3435-0455");
        item.put("title", "가락몰 빵축제 전국빵지자랑");
        item.put("mapx", "127.1107693087");
        item.put("mapy", "37.4960786971");
        items.put("item", List.of(item));
        responseBody.put("items", items);
        responseBody.put("numOfRows", 10);
        responseBody.put("pageNo", 1);
        responseBody.put("totalCount", 1);
        Map<String, Object> responseHeader = new HashMap<>();
        responseHeader.put("resultCode", "0000");
        responseHeader.put("resultMsg", "OK");
        Map<String, Object> response = new HashMap<>();
        response.put("header", responseHeader);
        response.put("body", responseBody);
        mockResponse.put("response", response);

        when(restTemplate.getForObject(any(String.class), eq(Map.class))).thenReturn(mockResponse);

        // when
        CompletableFuture<Void> future = festivalApiTasklet.fetchFestivalDataAsync(eventStartDate);
        future.join(); // 비동기 작업이 완료될 때까지 기다림

        // then
    }
}
