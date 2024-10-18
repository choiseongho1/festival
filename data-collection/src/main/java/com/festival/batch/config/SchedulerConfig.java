package com.festival.batch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.context.annotation.Bean;

/**
 * 스케줄러 설정 클래스
 * 비동기 작업을 활성화하고 스케줄러를 설정합니다.
 */
@Configuration
@EnableAsync
@EnableScheduling
public class SchedulerConfig {

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10); // 스레드 풀의 크기 설정
        scheduler.setThreadNamePrefix("Festival-Scheduler-"); // 스레드 이름 접두사 설정
        scheduler.setWaitForTasksToCompleteOnShutdown(true); // 종료 시 모든 작업이 완료될 때까지 대기
        scheduler.setAwaitTerminationSeconds(30); // 종료 대기 시간 설정
        return scheduler;
    }
}
