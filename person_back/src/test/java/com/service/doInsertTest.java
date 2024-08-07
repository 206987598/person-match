package com.service;


import com.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


@SpringBootTest
class doInsertTest {
    @Autowired
    private UserService userService;

    private ExecutorService executorService = new ThreadPoolExecutor(60, 1000, 10000, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000));

    /**
     * 并发多线程插入数据
     * 批量插入数据
     */
    @Test
    public void batchInsertUser() {
        // 创建一个计时器，用于测量程序运行时间
        StopWatch stopWatch = new StopWatch();
        // 开始计时
        stopWatch.start();
        // 定义每批次处理的用户数量
        int batchSize = 5000;
        // 用于记录已处理的用户数量，以便按批次提交
        int j = 0;
        // 用于存储所有异步任务的完成未来对象
        List<CompletableFuture<Void>> futureList = new ArrayList<>();

        // 模拟n次用户数据处理循环
        for (int i = 0; i < 10; i++) {
            // 创建一个用户列表，用于存储每批次的用户数据
            ArrayList<User> userList = new ArrayList<>();
            // 循环添加用户数据，直到达到批次大小
            while (true) {
                // 记录已处理的用户数量
                j++;
                // 创建一个新的用户对象
                User user = new User();
                // 设置用户属性，以下为示例数据
                user.setUsername("lackTest");
                user.setUserAccount("lack");
                user.setAvatarUrl("https://img1.baidu.com/it/u=1968668429,2104382916&fm=253&fmt=auto&app=138&f=JPEG?w=507&h=500");
                user.setGender(1);
                user.setUserPassword("12345678");
                user.setPhone("12345678");
                user.setEmail("12345678@qq.com");
                user.setUserStatus(0);
                user.setIsDelete(0);
                user.setUserRole(0);
                user.setPlanetCode("12345");
                user.setTags("");
                user.setProfile("");
                // 将用户对象添加到列表中
                userList.add(user);
                // 检查是否达到批次处理数量，达到则提交当前批次
                if (j % batchSize == 0) {
                    break;
                }
            }
            // 异步提交用户数据批量保存的任务
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                // 输出执行任务的线程名称
                System.out.println("ThreadName:" + Thread.currentThread().getName());
                // 调用用户服务的批量保存方法处理用户数据
                userService.saveBatch(userList, batchSize);
            }, executorService);
            // 将任务的未来对象添加到列表中
            futureList.add(future);
        }
        // 等待所有异步任务完成
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
        // 停止计时
        stopWatch.stop();
        // 输出程序总运行时间
        System.out.println(stopWatch.getTotalTimeMillis());

    }
}