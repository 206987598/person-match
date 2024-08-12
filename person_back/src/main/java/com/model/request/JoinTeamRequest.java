package com.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 加入队伍封装体
 *
 * @TableName team
 */

@Data
public class JoinTeamRequest implements Serializable {
    /**
     * 队伍id
     */
    private Long teamId;


    /**
     * 队伍密码
     */
    private String password;


}