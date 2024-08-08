package com.jobs;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.model.User;
import com.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
public class preRecommendUsers {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private List<Long> mainUserList = Arrays.asList(1L);

    @Scheduled(cron = "0 0 1 * * ?")
    public void doPreRecommendUsers() {
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

    }
}
