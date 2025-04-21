package com.ph.phojjudgeservice.judge.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Redisson配置类
 */
@Configuration
@Component
@Data
public class  redissonConfig {
    @Value("${spring.redis.host:localhost}")
    private String host;
    @Value("${spring.redis.port}")
    private Integer port;
    @Value("${spring.redis.database}")
    private Integer database;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String address = String.format("redis://%s:%s", host, port);
        config.useSingleServer()
                .setAddress(address) //设置地址
                .setDatabase(database);//设置数据库
        return Redisson.create(config);
    }
}
