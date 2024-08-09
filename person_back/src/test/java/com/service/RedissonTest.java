package com.service;


import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedissonTest {
    @Resource
    private RedissonClient redissonClient;

    @Test
    void test() {
        List<String> list = new ArrayList<>();
        list.add("lack");
        System.out.println(list.get(0));

//        list.remove(0);


        RList<String> rList = redissonClient.getList("RedissonTest");
        rList.add("lack");
        System.out.println(rList.get(0));
//        rList.remove(0);
    }

    @Test
    void watchDogs() {
        RLock lock = redissonClient.getLock("doPreRecommendUser:doCache:lock");
        //只有一个线程执行
        try {
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS))
                //不能使用debug调试卡住，会识别成服务器宕机。
                Thread.sleep(10000);
                System.out.println("开始"+Thread.currentThread().getId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 判断是否是到当前锁
            if (lock.isHeldByCurrentThread()) {
                System.out.println("结束"+Thread.currentThread().getId());
                // 释放锁
                lock.unlock();
            }
        }
    }
}
