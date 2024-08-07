package com.service;

import java.util.Date;

import com.mapper.UserMapper;
import com.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Resource
    private UserMapper userMapper;

    @Test
    public void testSave() {
        User user = new User();
        user.setUsername("lack");
        user.setUserAccount("lack");
        user.setAvatarUrl("https://static.res.qq.com/wupload/xy/pcdaohang/byRVXZiw.png");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setPhone("123456789");
        user.setEmail("123456789");
        user.setUserStatus(0);
        boolean result = userService.save(user);
        System.out.println(user.getId());
        assertAll(() -> assertTrue(result));
    }

    //    @Test
//    void userRegister() {
//        String userAccount = "la ck";
//        String userPassword = "12345678";
//        String checkPassword = "12345678";
//        long result = userService.userRegister(userAccount, userPassword, checkPassword);
//        assertAll(() -> assertEquals(-1, result));
//        if (result == -1){
//            System.out.println("测试通过");
//        }
//    }
    @Test
    public void searchUserByTags() {
        List<String> tagNameList = new ArrayList();
        tagNameList.add("java");
        tagNameList.add("python");
        List<User> userList = userService.searchUserByTags(tagNameList);
        System.out.println(userList);
    }

    /**
     * 单线程插入数据
     */
    @Test
    public void insertUser() {
        //单线程插入数据
        final int INSERT_NUM = 10000;
        int ACCOUNT_NUM = 0;
        ArrayList<User> userList = new ArrayList<>();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            ACCOUNT_NUM = i;
            user.setUsername("lackTest");
            user.setUserAccount("lack" + ACCOUNT_NUM);
            user.setAvatarUrl("https://img1.baidu.com/it/u=1968668429,2104382916&fm=253&fmt=auto&app=138&f=JPEG?w=507&h=500");
            user.setGender(0);
            user.setUserPassword("12345678");
            user.setPhone("12345678");
            user.setEmail("12345678@qq.com");
            user.setUserStatus(0);
            user.setIsDelete(0);
            user.setUserRole(0);
            user.setPlanetCode("12345" + ACCOUNT_NUM);
            user.setTags("");
            user.setProfile("");
            userList.add(user);
            userMapper.insert(user);
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

}