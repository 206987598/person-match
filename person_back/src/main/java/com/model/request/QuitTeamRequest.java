package com.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 退出队伍封装体
 *
 * @TableName team
 */

@Data
public class QuitTeamRequest implements Serializable {
    /**
     * 队伍id
     */
    private Long teamId;


    private static final long serialVersionUID = 1L;
}