package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.ErrorCode;
import com.exception.BusinessException;
import com.mapper.UserMapper;
import com.model.User;
import com.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.contant.UserConstant.USER_LOGIN_STATE;

/**
 * @author qings
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-07-17 17:00:26
 */

/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 盐值，混淆密码
     */
    private static final String passwordBefore = "lack";

    /**
     * /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param planetCode    星球编号
     * @return
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过长");
        }
        if (planetCode.length() > 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号过长");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不足8位");
        }
        //账号不能不包含特殊字符
        String accountRegex = "^[a-zA-Z0-9]+$";
        Matcher matcher = Pattern.compile(accountRegex).matcher(userAccount);
        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //校验账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);                  //这里使用的userMapper
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        //校验星球编号不能重复
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        count = userMapper.selectCount(queryWrapper);                  //这里使用的userMapper
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号重复");
        }
        //密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不相同");
        }
        //2.密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((passwordBefore + userPassword).getBytes());
        //3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);               //这里使用的ServiceImpl的save
        if (!saveResult) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "插入失败");
        }
        return user.getId();
    }

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @return
     */

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //2.密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((passwordBefore + userPassword).getBytes());
        //账号不能不包含特殊字符
        String accountRegex = "^[a-zA-Z0-9]+$";
        Matcher matcher = Pattern.compile(accountRegex).matcher(userAccount);
        if (!matcher.find()) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        //查看用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if (user == null) {
            log.info("账号或密码错误");
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        //3.用户脱敏
        User safetyUser = getSafetyUser(user);
        //4.记录用户的登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);

        return safetyUser;
    }

    /**
     * 用户脱敏
     *
     * @param orginUser
     * @return
     */
    @Override
    public User getSafetyUser(User orginUser) {
        if (orginUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        User safetyUser = new User();
        safetyUser.setId(orginUser.getId());
        safetyUser.setUsername(orginUser.getUsername());
        safetyUser.setUserAccount(orginUser.getUserAccount());
        safetyUser.setAvatarUrl(orginUser.getAvatarUrl());
        safetyUser.setPlanetCode(orginUser.getPlanetCode());
        safetyUser.setGender(orginUser.getGender());
        safetyUser.setPhone(orginUser.getPhone());
        safetyUser.setEmail(orginUser.getEmail());
        safetyUser.setUserRole(orginUser.getUserRole());
        safetyUser.setUserStatus(orginUser.getUserStatus());
        safetyUser.setCreateTime(orginUser.getCreateTime());
        return safetyUser;

    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @Override
    public int loginOut(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        throw new BusinessException(ErrorCode.PARAMS_ERROR);
    }
}




