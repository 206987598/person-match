package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.BaseResponse;
import com.common.ErrorCode;
import com.common.ResultUtils;
import com.exception.BusinessException;
import com.model.User;
import com.model.request.UserLoginRequest;
import com.model.request.UserRegisterRequest;
import com.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import static com.contant.UserConstant.USER_LOGIN_STATE;


/**
 * 用户控制器
 *
 * @author lack
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:5173/"}, allowCredentials = "true")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
//            return ResultUtils.error(ErrorCode.NULL_ERROR);
            throw new BusinessException(ErrorCode.NULL_ERROR, "参数为空");
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        int user = userService.loginOut(request);
        return ResultUtils.success(user);
    }

    /**
     * 获取当前用户
     *
     * @param request
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            return null;
        }
        Long userId = currentUser.getId();
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 用户查询
     *
     * @param username
     * @return
     */

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUser(String username, HttpServletRequest request) {
        // 仅允许管理员查询用户列表，确保权限安全
        if (!userService.isAdmin(request)) {
            return null;
        }
        // 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 如果用户名不为空，则添加模糊查询条件
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        // 执行查询，获取用户列表
        List<User> userList = userService.list(queryWrapper);
        // 处理用户列表，移除敏感信息
        List<User> list = userList.stream().map(user -> {
            // 清空用户密码
            user.setUserPassword(null);
            // 返回安全的用户信息
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());
        // 返回查询结果
        return ResultUtils.success(list);

    }

    @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommendUsers(long pageSize, long pageNum, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = userService.getLoginUser(request);
        //查缓存，如果有缓存直接取缓存
        //构造redisKey
        String redisKey = String.format("lack:user:recommend:%s", currentUser.getId());
        ValueOperations operations = redisTemplate.opsForValue();
        Page<User> userPage = (Page<User>) operations.get(redisKey);
        if (userPage != null) {
            return ResultUtils.success(userPage);
        }
        // 没有就直接查数据库
        // 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 执行查询，获取用户列表
         userPage = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
         // 缓存到redis
        try {
            operations.set(redisKey, userPage,1, TimeUnit.HOURS);
        } catch (Exception e) {
            log.error("redis set key error", e);
        }
        // 返回查询结果
        return ResultUtils.success(userPage);

    }

    /**
     * 根据标签搜索用户
     *
     * @param tagNameList
     * @return
     */
    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUserByTags(@RequestParam(required = false) List<String> tagNameList) {
        // 检查标签列表是否为空，如果为空则抛出业务异常
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        // 根据标签列表搜索用户
        List<User> userList = userService.searchUserByTags(tagNameList);
        // 返回搜索到的用户列表
        return ResultUtils.success(userList);

    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody Long id, HttpServletRequest request) {
        //仅管理员可删除
        if (!userService.isAdmin(request)) {
            return null;
        }
        if (id <= 0) {
            return null;
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);

    }

    /**
     * 更新用户
     *
     * @param user
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user, HttpServletRequest request) {
        // 检查用户对象是否为空
        if (user == null) {
            // 如果用户对象为空，则抛出空值异常
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        // 从请求中获取当前登录的用户
        User loginUser = userService.getLoginUser(request);
        // 检查获取的登录用户是否为空
        if (loginUser == null) {
            // 如果登录用户为空，则抛出未登录异常
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        // 更新用户信息，将传入的用户对象信息更新到数据库中，loginUser用于权限检查
        int result = userService.updateUser(user, loginUser);
        // 返回更新成功的提示信息
        return ResultUtils.success(result);
    }


}
