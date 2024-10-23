package com.festival.batch.tasklet;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.festival.datacollection.festival.domain.Festival;
import com.festival.datacollection.festival.dto.FestivalResponseDto;
import com.festival.datacollection.festival.dto.FestivalSaveDto;
import com.festival.datacollection.festival.repository.FestivalRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Component
public class FestivalApiTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(FestivalApiTasklet.class);
    private static final String TOURISM_API_URL = "https://apis.data.go.kr/B551011/KorService1/searchFestival1";
    private static final String SERVICE_KEY = "dhpf1HQ87LM1MoSC9A/2UvVHO/4MknltwpSRLiDjrQ7FvReSfDKBlpPjKBbRy/y56WmqHAyFqC1q/T/NNgxuFg==";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final FestivalRepository festivalRepository;

    public FestivalApiTasklet(FestivalRepository festivalRepository) {
        this.festivalRepository = festivalRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

        System.out.println("----------------> 배치 호출 시작");
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(15);
        LocalDate endDate = today.plusDays(15);

        IntStream.iterate(0, i -> i + 1)
                .limit(31)
                .forEach(i -> {
                    LocalDate targetDate = startDate.plusDays(i);
                    fetchFestivalDataAsync(targetDate.format(FORMATTER));
                });

        System.out.println("-------------> 배치 호출 종료");

        return RepeatStatus.FINISHED;
    }

    @Async
    public CompletableFuture<Void> fetchFestivalDataAsync(String eventStartDate) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        System.out.println("---------------> 일자 : " + eventStartDate);
        String url = TOURISM_API_URL + "?"
                + "MobileOS=WIN"
                + "&MobileApp=TEST"
                + "&eventStartDate=" + eventStartDate
                + "&ServiceKey=" + SERVICE_KEY;

        try {
            int pageNo = 1;
            boolean hasNextPage = true;
            XmlMapper xmlMapper = new XmlMapper();

            while (hasNextPage) {
                String pagedUrl = url + "&pageNo=" + pageNo;
                String responseJson = restTemplate.getForObject(pagedUrl, String.class);

                FestivalResponseDto responseDto = xmlMapper.readValue(responseJson, FestivalResponseDto.class);


                if (responseDto != null && responseDto.getBody() != null) {
                    FestivalResponseDto.Body body = responseDto.getBody();
                    FestivalResponseDto.Items items = body.getItems();

                    if (items != null && items.getItemList() != null) {
                        for (FestivalResponseDto.FestivalItem item : items.getItemList()) {
                            FestivalSaveDto festivalDto = FestivalSaveDto.builder()
                                    .addr1(item.getAddr1())
                                    .addr2(item.getAddr2())
                                    .contentid(item.getContentid())
                                    .eventstartdate(item.getEventstartdate())
                                    .eventenddate(item.getEventenddate())
                                    .firstimage(item.getFirstimage())
                                    .firstimage2(item.getFirstimage2())
                                    .tel(item.getTel())
                                    .title(item.getTitle())
                                    .mapx(item.getMapx())
                                    .mapy(item.getMapy())
                                    .build();

                            Festival festival = festivalDto.toEntity();
                            festivalRepository.save(festival);
//                            logger.info("축제 정보 저장 완료: {}", festival.getTitle());
                        }
                    }

                    int numOfRows = body.getNumOfRows();
                    int currentPage = body.getPageNo();
                    int totalCount = body.getTotalCount();

                    hasNextPage = (currentPage * numOfRows) < totalCount;
                    pageNo++;
                } else {
                    hasNextPage = false;
                }
            }
        } catch (Exception e) {
            logger.error("API 호출 실패 (날짜: {})", eventStartDate, e);
        }

        return CompletableFuture.completedFuture(null);
    }
}