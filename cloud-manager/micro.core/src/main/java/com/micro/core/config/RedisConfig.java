package com.micro.core.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class RedisConfig
{
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.maxConnect}")
    private int clientcount;

    @Value("${spring.redis.maxConnect}")
    private int maxConnect;


    @Value("${spring.redis.maxIdle}")
    private Integer maxIdle;

    @Value("${spring.redis.maxWaitMillis}")
    private Integer maxWaitMillis;
}

