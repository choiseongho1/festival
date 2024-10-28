package com.festival.batch.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 축제 배치 작업을 스케줄링하는 클래스
 * 정해진 시간에 배치를 실행합니다.
 */
@Component
public class FestivalScheduler {

    private static final Logger logger = LoggerFactory.getLogger(FestivalScheduler.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("festivalJobBean")
    private Job festivalJob;

//    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
//    오후 3시 30분에 실행되는 스케줄러 설정 (cron 표현식)
    @Scheduled(cron = "0 58 9 * * ?\n")
    public void runFestivalJob() {
        try {
            jobLauncher.run(festivalJob, new org.springframework.batch.core.JobParameters());
            logger.info("축제 배치 작업 실행 완료");
        } catch (Exception e) {
            logger.error("축제 배치 작업 실행 중 오류 발생", e);
        }
    }

}
