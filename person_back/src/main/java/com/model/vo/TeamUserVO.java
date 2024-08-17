package com.model.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 队伍和用户信息封装类
 *
 * @TableName team
 */
@Data
public class TeamUserVO implements Serializable {
    /**
     * id
     */
    private Long id;


    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 已加入的用户列表
     */
    private List<UserVO> userVOList;

    /**
     * 创建人信息
     */
    private UserVO createUserVO;

    /**
     * 已加入人数
     */
    private long hasJoinNum;
    /**
     * 是否加入
     */
    private boolean hasJoin = false;

    private static final long serialVersionUID = 1L;
}