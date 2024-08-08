package com.service;

import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class RedissonTest {
    @Resource
    private RedissonClient redissonClient;
    @Test
    void test(){
        List<String> list=new ArrayList<>();
        list.add("lack");
        System.out.println(list.get(0));

//        list.remove(0);


        RList<String> rList= redissonClient.getList("RedissonTest");
        rList.add("lack");
        System.out.println(rList.get(0));
//        rList.remove(0);
    }
}
