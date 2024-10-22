package com.festival.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 배치 작업 설정 클래스
 * 배치 작업과 스케줄링을 활성화합니다.
 */
@Configuration
@EnableBatchProcessing
public class BatchJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public BatchJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job festivalJob() {
        JobBuilder jobBuilder = new JobBuilder("festivalJob", jobRepository);
        SimpleJobBuilder simpleJobBuilder = jobBuilder.start(festivalStep());
        return simpleJobBuilder.build();
    }

    @Bean
    public Step festivalStep() {
        return new StepBuilder("festivalStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Festival Step 실행 중...");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}