package com.festival.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 애플리케이션 시작 시 배치 작업을 실행하는 클래스입니다.
 */
@Component
public class BatchJobRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(BatchJobRunner.class);

    private final JobLauncher jobLauncher;
    private final Job festivalJob;

    public BatchJobRunner(JobLauncher jobLauncher, Job festivalJob) {
        this.jobLauncher = jobLauncher;
        this.festivalJob = festivalJob;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            jobLauncher.run(festivalJob, new JobParameters());
            logger.info("축제 배치 작업이 애플리케이션 시작 시 실행되었습니다.");
        } catch (Exception e) {
            logger.error("축제 배치 작업 실행 중 오류가 발생했습니다.", e);
        }
    }
}
