package com.festival.batch.config;

import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBatchTest
@TestConfiguration
//@Import(BatchJobConfig.class) // 실제 Batch 설정 파일을 가져옵니다.
public class BatchTestConfig {

    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils() {
        return new JobLauncherTestUtils();
    }
}
