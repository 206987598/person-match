package com.service;

import com.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

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
}