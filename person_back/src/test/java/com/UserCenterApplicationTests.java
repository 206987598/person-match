package com;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

@SpringBootTest
class UserCenterApplicationTests {
    @Test
    /**
     * 测试加密
     */
    void testPassword() {
        String password = "123456";
        String md5 = DigestUtils.md5DigestAsHex(password.getBytes());
        System.out.println(md5);
    }

    @Test
    void contextLoads() {
    }

}
