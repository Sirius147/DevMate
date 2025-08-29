package com.sirius.DevMate.config.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Integer redisPort;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory cf) {
        var template = new StringRedisTemplate();
        template.setConnectionFactory(cf);
        // 기본 직렬화는 String. JWT 해시/키 저장에 충분함
        return template;

    }
}