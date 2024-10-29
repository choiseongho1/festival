package com.festival.apigateway.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 연결 및 설정을 위한 Configuration 클래스
 * Redis는 리프레시 토큰을 저장하고 관리하는데 사용됩니다.
 */
@Configuration
public class RedisConfig {
    
    /**
     * application.yml에서 설정된 Redis 호스트 주소
     * 기본값: localhost
     */
    @Value("${spring.redis.host}")
    private String redisHost;
    
    /**
     * application.yml에서 설정된 Redis 포트 번호
     * 기본값: 6379
     */
    @Value("${spring.redis.port}")
    private int redisPort;
    
    /**
     * Redis 연결을 위한 Factory 빈 생성
     * Lettuce 라이브러리를 사용하여 Redis에 연결합니다.
     * 
     * @return RedisConnectionFactory 객체
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }
    
    /**
     * Redis 데이터 처리를 위한 템플릿 빈 생성
     * key와 value 모두 String 타입으로 직렬화하여 저장합니다.
     * 
     * @return RedisTemplate<String, String> 객체
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        
        // Redis 연결 Factory 설정
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        
        // key와 value의 직렬화 방식을 String 타입으로 설정
        // Redis에 저장될 때 문자열 형태로 저장되도록 함
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        
        return redisTemplate;
    }
}