package com.festival.batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncBatchConfig {


    /**
     * 비동기 배치 처리를 위한 TaskExecutor 빈 설정
     *
     * @return ThreadPoolTaskExecutor 인스턴스
     */
    @Bean(name = "batchTaskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);  // 기본 스레드 개수
        executor.setMaxPoolSize(20);   // 최대 스레드 개수
        executor.setQueueCapacity(50); // 큐의 용량 설정
        executor.setThreadNamePrefix("Batch-Executor-"); // 스레드 이름 접두사 설정
        executor.initialize();
        return executor;
    }
}
