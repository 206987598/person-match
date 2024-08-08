package com.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Redisson配置
 */
@Configuration
// 读取配置文件
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {
    private String host;
    private String port;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String RedisAddress = "redis://" + host + ":" + port;
        config.useSingleServer().setAddress(RedisAddress).setPassword("123456").setDatabase(2);
        // 创建RedissonClient对象
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
