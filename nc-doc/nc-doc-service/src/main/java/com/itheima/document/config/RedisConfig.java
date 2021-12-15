package com.itheima.document.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式锁配置
 */

@Configuration
public class RedisConfig {

    @Bean
    public RedissonClient redissonClient(RedisProperties prop) {
        String address = "redis://%s:%d";
        Config config = new Config();
        config.useSingleServer()
                .setAddress(String.format(address, prop.getHost(), prop.getPort()));
        return Redisson.create(config);
    }
}