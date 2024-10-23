package com.festival.datacollection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"com.festival.datacollection", "com.festival.batch"})  // 필요한 패키지를 스캔
@EnableAsync(proxyTargetClass = true)
public class DataCollectionApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataCollectionApplication.class, args);
    }
}