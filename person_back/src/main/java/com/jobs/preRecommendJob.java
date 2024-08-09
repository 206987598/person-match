package com.jobs;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.model.User;
import com.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class preRecommendJob {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private RedissonClient redissonClient;

    private List<Long> mainUserList = Arrays.asList(1L);

    @Scheduled(cron = "0 0 1 * * ?")
    public void doPreRecommendUsers() {
        RLock lock = redissonClient.getLock("doPreRecommendUser:doCache:lock");
        //只有一个线程执行
        try {
            if (lock.tryLock(0, 3000, TimeUnit.MILLISECONDS))
                for (Long userId : mainUserList) {
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    // 执行查询，获取用户列表
                    Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);
                    // 获取当前用户
                    String redisKey = String.format("lack:user:recommend:%s", userId);
                    ValueOperations operations = redisTemplate.opsForValue();
                    // 缓存到redis
                    try {
                        operations.set(redisKey, userPage, 1, TimeUnit.HOURS);
                    } catch (Exception e) {
                        log.error("redis set key error", e);
                    }
                }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 判断是否是到当前锁
            if (lock.isHeldByCurrentThread()) {
                // 释放锁
                lock.unlock();
            }
        }
    }
}
