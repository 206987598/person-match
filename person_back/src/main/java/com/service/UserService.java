package com.service;

import com.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qings
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2024-07-17 17:00:26
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param planetCode     星球编号
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword , String planetCode);

    /**
     * 登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request      请求域
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param orginUser
     * @return
     */
    User getSafetyUser(User orginUser);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    int loginOut(HttpServletRequest request);
}
