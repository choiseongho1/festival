package com.festival.batch.config;

import com.festival.batch.tasklet.FestivalApiTasklet;
import com.festival.datacollection.festival.repository.FestivalRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 배치 작업 설정 클래스
 * 배치 작업과 스케줄링을 활성화합니다.
 */
@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final FestivalRepository festivalRepository;

    public BatchJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager, FestivalRepository festivalRepository) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.festivalRepository = festivalRepository;
    }

    @Bean
    public Job festivalJob() {
        return new JobBuilder("festivalJob", jobRepository)
                .start(festivalStep())
                .build();
    }

    @Bean
    public Step festivalStep() {
        return new StepBuilder("festivalStep", jobRepository)
                .tasklet(festivalApiTasklet(), transactionManager)
                .build();
    }

    @Bean
    public FestivalApiTasklet festivalApiTasklet() {
        return new FestivalApiTasklet(festivalRepository);
    }
}
