package com.festival.batch.job;

import com.festival.batch.tasklet.FestivalApiTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 축제 배치 작업을 정의하는 클래스
 * Job과 Step을 설정합니다.
 */
@Configuration
public class FestivalJob {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final FestivalApiTasklet festivalApiTasklet;

    public FestivalJob(JobRepository jobRepository,
                       PlatformTransactionManager transactionManager,
                       FestivalApiTasklet festivalApiTasklet) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.festivalApiTasklet = festivalApiTasklet;
    }
    @Bean(name = "festivalJobBean")
    public Job festivalJob(@Qualifier("festivalStepBean") Step festivalStep) {  // Step을 생성자에서 주입
        return new JobBuilder("festivalJob", jobRepository)
                .start(festivalStep)
                .build();
    }

    @Bean(name = "festivalStepBean")
    public Step festivalStep() {
        return new StepBuilder("festivalStep", jobRepository)
                .tasklet(festivalApiTasklet, transactionManager)
                .build();
    }
}