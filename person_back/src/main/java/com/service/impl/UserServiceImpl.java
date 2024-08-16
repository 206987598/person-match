package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.DistanceUtils;
import com.common.ErrorCode;
import com.exception.BusinessException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapper.UserMapper;
import com.model.User;
import com.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jboss.marshalling.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.contant.UserConstant.ADMIN_ROLE;
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
        safetyUser.setTags(orginUser.getTags());
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
        return 1;
    }

    /**
     * 根据标签搜索用户（内存过滤）
     *
     * @param tagNameList
     * @return
     */

    @Override
    public List<User> searchUserByTags(List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //1.获取所有用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);
        Gson gson = new Gson();
        //2.在内存中判断是否有符合要求的标签
        return userList.stream().filter(user -> {
            String tagsStr = user.getTags();
            if (StringUtils.isBlank(tagsStr)) {
                return false;
            }
            // 使用Gson从字符串中反序列化出用户列表
            Set<String> tempTagSet = gson.fromJson(tagsStr, new TypeToken<Set<String>>() {
            }.getType());
            tempTagSet = Optional.ofNullable(tempTagSet).orElse(new HashSet<>());
            for (String tagName : tagNameList) {
                if (!tempTagSet.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).map(this::getSafetyUser).collect(Collectors.toList());
//        return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());
    }

    /**
     * 查询当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 检查请求对象是否为空，为空则抛出参数错误异常
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 从会话中获取用户对象，如果为空则抛出未登录异常
        Object loginUser = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN);
        }
        // 将用户对象转换为User类型并返回
        return (User) loginUser;

    }

    /**
     * 更新用户信息
     *
     * @param user
     * @param loginUser
     * @return
     */
    @Override
    public int updateUser(User user, User loginUser) {
        // 检查用户ID是否为空，为空则抛出参数错误异常
        long userId = user.getId();
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断是否为管理员或者是否为本人
        if (!isAdmin(loginUser) && userId != loginUser.getId()) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        //查询用户
        User oldUser = userMapper.selectById(user);
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        // 更新用户信息，返回更新结果
        return userMapper.updateById(user);

    }

    /**
     * 判断是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 从会话中获取用户对象，该对象代表当前登录的用户状态
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        // 将获取的用户对象转换为User类型，以便进行进一步的验证
        User user = (User) userObj;
        // 检查用户对象是否存在，以及用户角色是否为管理员角色
        // 这一步确保只有管理员角色的用户才能通过验证
        if (user == null || user.getUserRole() != ADMIN_ROLE) {
            // 如果用户对象不存在或用户角色不是管理员，返回false，表示验证不通过
            return false;
        }
        // 如果用户对象存在且用户角色是管理员，返回true，表示验证通过
        return true;

    }

    /**
     * 判断当前是否为管理员
     *
     * @param loginUser
     * @return
     */
    @Override
    public boolean isAdmin(User loginUser) {
        // 检查当前登录用户是否为空或不是管理员角色
        // 这里解释了代码的目的：判断用户是否有权限执行某些操作
        // loginUser 参数：当前登录的用户对象
        // ADMIN_ROLE 常量：表示管理员角色的标识
        return loginUser == null || loginUser.getUserRole() != ADMIN_ROLE;

    }

    /**
     * 根据标签搜索用户 (通过SQL实现)
     *
     * @param tagNameList
     * @return
     */

    @Deprecated
    private List<User> searchUserByTagsBySQL(List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        for (String tagName : tagNameList) {
            //模糊查询   拼接and查询
            // tags like '%java%' and tags like '%python%'
            queryWrapper = queryWrapper.like("tags", tagName);
        }
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(user -> {
            getSafetyUser(user);
        });
        return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());
    }

    /**
     * 获取匹配用户
     *
     * @param num
     * @param loginUser
     * @return
     */

    @Override
    public List<User> matchUsers(long num, User loginUser) {
        // 创建查询包装器，用于查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 选择查询字段为id和tags
        queryWrapper.select("id", "tags");
        // 过滤条件：tags不为空
        queryWrapper.isNotNull("tags");
        // 获取所有用户列表
        List<User> userList = this.list();
        // 获取当前登录用户的标签字符串
        String tags = loginUser.getTags();
        // 创建Gson对象用于JSON解析
        Gson gson = new Gson();
        // 将标签字符串解析为标签列表
        List<String> tagNameList = gson.fromJson(tags, new TypeToken<List<String>>() {
        }.getType());
        // 创建用户与距离的配对列表
        List<Pair<User, Long>> list = new ArrayList<>();
//        // 创建一个排序映射，用于存储用户索引和标签距离
//        SortedMap<Integer, Long> indexDistanceMap = new TreeMap<>();
        // 遍历用户列表
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            // 获取当前用户的标签字符串
            String userTags = user.getTags();
            // 如果用户没有标签，则跳过
            if (StringUtils.isBlank(userTags) || user.getId() == loginUser.getId()) {
                continue;
            }
            // 将用户标签字符串解析为标签列表
            List<String> userTagList = gson.fromJson(userTags, new TypeToken<List<String>>() {
            }.getType());
            // 计算当前用户标签与登录用户标签的最小距离
            long minDistance = DistanceUtils.minDistance(tagNameList, userTagList);
//            // 将用户索引和最小距离存入映射
//            indexDistanceMap.put(i, minDistance);
            // 将用户和距离添加到配对列表
            list.add(new Pair<>(user, minDistance));
        }
        //按编辑距离进行升序
        List<Pair<User, Long>> topUserPairList = list.stream()
                .sorted((a, b) -> (int) (a.getB() - b.getB()))
                .limit(num)
                .collect(Collectors.toList());

//        // 创建一个列表来收集最接近的用户
//        List<User> collectList = new ArrayList<>();
//        int i = 0;
//        // 遍历排序后的用户索引和距离映射
//        for (Map.Entry<Integer, Long> entry : indexDistanceMap.entrySet()) {
//            // 如果收集的用户数量已达到指定数量，则停止遍历
//            if (i > num) {
//                break;
//            }
//            // 根据索引获取用户
//            User user = userList.get(entry.getKey());
//            // 输出用户ID和距离信息（此处的输出根据实际需求可能需要调整）
//            System.out.println(user.getId() + ";" + entry.getKey() + ":" + entry.getValue());
//
//            // 增加收集的用户数量
//            i++;
//    }
        // 将排序后的用户配对列表转换为用户列表
        //原本的列表是
        List<Long> collectList = topUserPairList.stream().map(pair -> pair.getA().getId()).collect(Collectors.toList());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("id", collectList);
        // 将用户列表转换为Map，键为用户ID，值为用户对象
        //1,2,3
        //1->User1,2->User2,3->User3
        Map<Long, List<User>> users = this.list(userQueryWrapper)
                .stream().map(user -> getSafetyUser(user))
                .collect(Collectors.groupingBy(User::getId));
        List<User> finallyUserList = new ArrayList<>();
        for (Long userId : collectList) {
            finallyUserList.add(users.get(userId).get(0));
        }
        // 返回最接近的用户列表
        return finallyUserList;
    }


}




